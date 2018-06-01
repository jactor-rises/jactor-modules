package com.github.jactor.rises.persistence.orm;

import com.github.jactor.rises.persistence.orm.dao.HibernateRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static java.util.Arrays.sort;

@SpringBootApplication
@EnableTransactionManagement
public class PersistenceOrmApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceOrmApplication.class);

    public static void main(String... args) {
        display(SpringApplication.run(PersistenceOrmApplication.class, args));
    }

    private static void display(ApplicationContext ctx) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Available beans:");

            String[] beanDefinitions = ctx.getBeanDefinitionNames();
            sort(beanDefinitions);

            for (String beanDefinition : beanDefinitions) {
                LOGGER.info("-> " + beanDefinition);
            }
        }
    }

    @Bean
    public HibernateRepository hibernateRepository(SessionFactory sessionFactory) {
        return new HibernateRepository(sessionFactory);
    }
}
