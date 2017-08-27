package com.github.jactorrises.facade.config;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.orm.domain.DefaultUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.criterion.Restrictions.eq;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JactorDbContext.class)
@Transactional
public class DatabasePocTest {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name = "dataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before public void initJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test public void willFindUsersInTheDatabase() {
        @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"}) int noOfRows = jdbcTemplate.queryForObject("SELECT count(1) FROM t_user", Integer.class);
        createUserInTheDatabaseWith(new UserName("svada"));
        createUserInTheDatabaseWith(new UserName("lada"));

        assertThat(session().createCriteria(UserEntity.class).list()).hasSize(noOfRows + 2);
    }

    @Test public void willReadDatabaseValues() {
        createUserInTheDatabaseWith(new UserName("testing"));
        UserEntity user = (UserEntity) session().createCriteria(DefaultUserEntity.class).add(eq("emailAddress", "testing@svada.lada")).uniqueResult();

        assertThat(user.getPassword()).isEqualTo("testing");
    }

    private void createUserInTheDatabaseWith(UserName userName) {
        DefaultUserEntity userEntity = new DefaultUserEntity();
        userEntity.setEmailAddress(userName.getName() + "@svada.lada");
        userEntity.setPassword(userName.getName());
        userEntity.setUserName(userName.getName());

        session().save(userEntity);
        session().flush();
        session().clear();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
