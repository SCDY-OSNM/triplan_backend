package scdy.contentsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContentsServiceApplication

fun main(args: Array<String>) {
    runApplication<ContentsServiceApplication>(*args)
}
