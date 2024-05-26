package com.github.jactor.persistence

import java.text.SimpleDateFormat
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence", version = "v1"))
class JactorPersistenceConfig {

    @Bean
    fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner {
        return CommandLineRunner {
            JactorPersistence.inspect(applicationContext, it)
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd"))
        .setSerializationInclusion(Include.NON_NULL)
}
