package scdy.contentsservice.common.config


import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories(basePackages = ["scdy.contentsservice.repository.elasticSearch"])
class ElasticSearchConfig {
    /*
        @Bean

        fun client(): ElasticsearchClient {
            val transport = RestClientTransport(
                    RestClient.builder(HttpHost("localhost", 9200, "http")).build(),
                    JacksonJsonpMapper()
            )
            return ElasticsearchClient(transport)
        }
    fun client(): ElasticsearchClient {
        val credentialsProvider = BasicCredentialsProvider().apply {
            setCredentials(AuthScope.ANY, UsernamePasswordCredentials("elastic", "yearm404"))
        }

    @Bean(name = ["elasticsearchTemplate"])
    fun elasticsearchOperations(): ElasticsearchOperations {
        return ElasticsearchTemplate(client())
    }*/
}