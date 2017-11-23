package com.github.jactorrises.persistence;

import com.github.jactorrises.persistence.client.dao.PersistentDao;
import com.github.jactorrises.persistence.repository.HibernateRepository;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.sort;

@SpringBootApplication
public class JactorPersistence {

    private static final Logger LOGGER = Logger.getLogger(JactorPersistence.class);

    public static void main(String... args) {
        display(SpringApplication.run(JactorPersistence.class, args));
    }

    private static void display(ApplicationContext ctx) {
        LOGGER.debug("Available beans:");

        String[] beanDefinitions = ctx.getBeanDefinitionNames();
        sort(beanDefinitions);

        LOGGER.debug(namesOf(beanDefinitions));
    }

    private static StringBuilder namesOf(String[] beanNames) {
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

        return names;
    }

    @Bean
    public PersistentDao persistentDao(SessionFactory sessionFactory) {
        return new HibernateRepository(sessionFactory);
    }
}
