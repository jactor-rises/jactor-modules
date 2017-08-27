package com.github.jactorrises.facade.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JactorDbContext {

    @Bean(name = "dataSource") public DataSource dataSourceFromHsqldb() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("mydb" + System.currentTimeMillis())
                .addScript("classpath:create.db.sql")
                .addScript("classpath:create.constraints.sql")
                .addScript("classpath:create.default.users.sql")
                .build();
    }

    @Bean(name = "sessionFactory") public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("com.github.jactorrises.persistence.orm.domain");
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", "validate");
                setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
                setProperty("hibernate.globally_quoted_identifiers", "true");
            }
        });

        return sessionFactory;
    }

    @Bean(name = "txManager") public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }
}
