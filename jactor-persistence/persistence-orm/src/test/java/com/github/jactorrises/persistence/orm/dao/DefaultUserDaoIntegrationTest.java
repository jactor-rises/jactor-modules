package com.github.jactorrises.persistence.orm.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.orm.domain.DefaultUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("fix after finished making a persistence layer with spring-boot")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultUserDaoIntegrationTest.JactorDbContext.class)
@Transactional
public class DefaultUserDaoIntegrationTest {

    private DefaultUserDao userDaoToTest;

    @Resource(mappedName = "sessionFactory") private SessionFactory sessionFactory;

    @Before public void initDao() {
        userDaoToTest = new DefaultUserDao(sessionFactory);
    }

    @Test public void willPersistAndFindUserWithDao() {
        DefaultUserEntity userEntity = new DefaultUserEntity();
        userEntity.setUserName("me");
        userEntity.setPassword("demo");
        session().save(userEntity);

        session().flush();
        session().clear();

        assertThat(userDaoToTest.findUsing(new UserName("me"))).isNotNull();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @ContextConfiguration
    public static class JactorDbContext {

        @Bean(name = "dataSource") public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.HSQL)
                    .setName("mydb-" + System.currentTimeMillis())
                    .addScript("classpath:create.db.sql")
                    .addScript("classpath:create.constraints.sql")
                    .addScript("classpath:create.default.users.sql")
                    .build();
        }

        @Bean(name = "sessionFactoryBean") public LocalSessionFactoryBean locaslSessionFactoryBean() {
            LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
            sessionFactory.setDataSource(dataSource());
            sessionFactory.setPackagesToScan("com.github.jactor-rises.persistence.domain");
            sessionFactory.setHibernateProperties(new Properties() {
                {
                    setProperty("hibernate.hbm2ddl.auto", "validate");
                    setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
                    setProperty("hibernate.globally_quoted_identifiers", "true");
                }
            });

            return sessionFactory;
        }

        @Bean(name = "sessionFactory") public SessionFactory sessionFactory() {
            return locaslSessionFactoryBean().getObject();
        }

        @Bean(name = "txManager") public HibernateTransactionManager txManager(LocalSessionFactoryBean localSessionFactoryBean) {
            HibernateTransactionManager txManager = new HibernateTransactionManager();
            txManager.setSessionFactory(localSessionFactoryBean.getObject());

            return txManager;
        }
    }
}
