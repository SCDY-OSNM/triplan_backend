package scdy.boardservice.common.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories


@Configuration
@EnableElasticsearchRepositories(basePackages = ["scdy.boardservice.repository"])
@ComponentScan("scdy.boardservice")
class ElasticsearchConfig  {
    //ElasticsearchConfiguration()
//    override fun clientConfiguration(): ClientConfiguration {
//        return ClientConfiguration.builder()
//            .connectedTo("localhost:9200")
//            .build()
//    }
}