package com.github.jactorrises.persistence;

import com.github.jactorrises.persistence.dao.HibernateRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.sort;

@SpringBootApplication
public class JactorPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(JactorPersistence.class);

    public static void main(String... args) {
        display(SpringApplication.run(JactorPersistence.class, args));
    }

    private static void display(ApplicationContext ctx) {
        LOGGER.debug("Available beans:");

        String[] beanDefinitions = ctx.getBeanDefinitionNames();
        sort(beanDefinitions);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(namesOf(beanDefinitions));
        }
    }

    private static String namesOf(String[] beanNames) {
        StringBuilder names = new StringBuilder();

        for (int i = 0; i < beanNames.length; i++) {
            String name = beanNames[i];

            if (i % 5 == 0 || name.contains(".")) {
                names.append("\n-> ");
            }

            names.append(name);

            if (i % 5 == 0) {
                names.append(", ");
            }
        }

        return names.toString();
    }

    @Bean
    public HibernateRepository hibernateRepository(SessionFactory sessionFactory) {
        return new HibernateRepository(sessionFactory);
    }
}
