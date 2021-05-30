package com.github.jactor.persistence;

import static java.util.Arrays.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JactorPersistence {

  private static final Logger LOGGER = LoggerFactory.getLogger(JactorPersistence.class);

  static void inspect(ApplicationContext applicationContext, String[] args) {
    if (LOGGER.isDebugEnabled()) {
      boolean noArgs = args == null || args.length == 0;
      String arguments = noArgs ? "without arguments!" : "with arguments: " + String.join(" ", args) + '!';

      LOGGER.debug("Starting application {}", arguments);

      SpringBeanNames springBeanNames = new SpringBeanNames();
      stream(applicationContext.getBeanDefinitionNames()).sorted().forEach(springBeanNames::add);

      LOGGER.debug("Available beans (only simple names):");
      springBeanNames.listBeanNames().stream().map(name -> "- " + name).forEach(LOGGER::debug);

      LOGGER.debug("Ready for service...");
    }
  }

  public static void main(String... args) {
    SpringApplication.run(JactorPersistence.class, args);
  }
}
