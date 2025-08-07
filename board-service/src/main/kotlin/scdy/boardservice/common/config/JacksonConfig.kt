package scdy.boardservice.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
class JacksonConfig {
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        val objectMapper = ObjectMapper()
//        objectMapper.registerModule(JavaTimeModule())
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // ISO-8601 형식 유지
//        objectMapper.registerModule(KotlinModule.Builder().build()) // Kotlin 사용 시 (순서 무관)
//        return objectMapper
//    }
@Bean
fun objectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    val javaTimeModule = JavaTimeModule()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    javaTimeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
    objectMapper.registerModule(javaTimeModule)
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    objectMapper.registerModule(KotlinModule.Builder().build())
    return objectMapper
}

}