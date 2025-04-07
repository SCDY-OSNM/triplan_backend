package scdy.contentsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories( // JPA 관련 Repository 스캔
        basePackages = ["scdy.contentsservice.repository"]
)
@SpringBootApplication
class ContentsServiceApplication

fun main(args: Array<String>) {
    runApplication<ContentsServiceApplication>(*args)
}
