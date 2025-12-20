package com.github.jactor.rises.persistence.config

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.jactor.rises.persistence.JactorPersistence
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence.misc", version = "v2"))
class JactorPersistenceConfig {
    @Bean
    fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner = CommandLineRunner {
        JactorPersistence.inspect(applicationContext, it)
    }

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd"))
        .setSerializationInclusion(Include.NON_NULL)
}
