package com.github.jactor.persistence

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence", version = "v1"))
class JactorPersistenceConfig {

  @Bean
  fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner {
    return CommandLineRunner {
      JactorPersistence.inspect(applicationContext, it)
    }
  }
}
