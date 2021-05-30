package com.github.jactor.persistence;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "jactor-persistenxe", version = "v1")
)
public class JactorPersistenceConfig {

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
    return args -> JactorPersistence.inspect(applicationContext, args);
  }
}
