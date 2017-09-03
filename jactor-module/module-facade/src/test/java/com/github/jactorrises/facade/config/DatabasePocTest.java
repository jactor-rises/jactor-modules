package com.github.jactorrises.facade.config;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.boot.HibernateConfiguration;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.criterion.Restrictions.eq;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HibernateConfiguration.class)
@Transactional
public class DatabasePocTest {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Test public void willFindUsersInTheDatabase() {
        int noOfRows = session().createCriteria(UserEntity.class).list().size();
        createUserInTheDatabaseWith(new UserName("svada"));
        createUserInTheDatabaseWith(new UserName("lada"));

        assertThat(session().createCriteria(UserEntity.class).list()).hasSize(noOfRows + 2);
    }

    @Test public void willReadDatabaseValues() {
        createUserInTheDatabaseWith(new UserName("testing"));
        UserEntity user = (UserEntity) session().createCriteria(UserEntityImpl.class).add(eq("emailAddress", "testing@svada.lada")).uniqueResult();

        assertThat(user.getPassword()).isEqualTo("testing");
    }

    private void createUserInTheDatabaseWith(UserName userName) {
        UserEntity userEntity = new UserEntityImpl();
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
