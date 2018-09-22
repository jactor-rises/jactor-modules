package com.gitlab.jactor.rises.persistence;

import com.gitlab.jactor.rises.commons.framework.SpringBeanNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.stream;

@SpringBootApplication
public class JactorPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(JactorPersistence.class);

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
        return args -> inspect(applicationContext, args);
    }

    private void inspect(ApplicationContext applicationContext, String[] args) {
        if (LOGGER.isDebugEnabled()) {
            boolean noArgs = args == null || args.length == 0;
            String arguments = noArgs ? "without arguments!" : "with arguments: " + String.join(" ", args) + '!';

            LOGGER.debug("Starting {}", arguments);

            SpringBeanNames springBeanNames = new SpringBeanNames();
            stream(applicationContext.getBeanDefinitionNames()).sorted().forEach(springBeanNames::add);

            LOGGER.debug("Available beans:");
            springBeanNames.getBeanNames().stream().map(name -> "- " + name).forEach(LOGGER::debug);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Available spring beans:");
                springBeanNames.getNamesOfSpringBeans().stream().map(name -> "- " + name).forEach(LOGGER::trace);
            }
        }
    }

    public static void main(String... args) {
        SpringApplication.run(JactorPersistence.class, args);
    }
}
