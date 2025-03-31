package scdy.boardservice.common.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import jakarta.annotation.PreDestroy
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import java.io.IOException

@Configuration
class RestClientConfig {

    @Value("\${spring.elasticsearch.uris}")
    private lateinit var elasticsearchUrl: String

    @Value("\${spring.elasticsearch.username}")
    private lateinit var username: String

    @Value("\${spring.elasticsearch.password}")
    private lateinit var password: String

    private var httpClient: CloseableHttpAsyncClient? = null

    @Bean
    fun elasticsearchClient(): ElasticsearchClient {
        val credentialsProvider = BasicCredentialsProvider().apply {
            setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username, password))
        }

        httpClient = HttpAsyncClientBuilder.create()
            .setDefaultCredentialsProvider(credentialsProvider)
            .build()

        val transport = RestClientTransport(
            RestClient.builder(HttpHost.create(elasticsearchUrl))
                .setHttpClientConfigCallback { it.setDefaultCredentialsProvider(credentialsProvider) }
                .build(),
            JacksonJsonpMapper()
        )

        return ElasticsearchClient(transport)
    }

    @Bean
    fun elasticsearchTemplate(elasticsearchClient: ElasticsearchClient): ElasticsearchTemplate {
        return ElasticsearchTemplate(elasticsearchClient)
    }

    @PreDestroy
    fun closeHttpClient() {
        try {
            httpClient?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}