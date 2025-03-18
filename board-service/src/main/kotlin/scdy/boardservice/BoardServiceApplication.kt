package scdy.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BoardServiceApplication

fun main(args: Array<String>) {
    runApplication<BoardServiceApplication>(*args)
}
