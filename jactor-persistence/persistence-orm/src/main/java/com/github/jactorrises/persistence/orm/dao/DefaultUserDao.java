package com.github.jactorrises.persistence.orm.dao;

import com.github.jactorrises.persistence.orm.domain.DefaultUserEntity;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.client.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

import static org.hibernate.criterion.Restrictions.eq;

public class DefaultUserDao implements UserDao {

    private final SessionFactory sessionFactory;

    DefaultUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override public Optional<UserEntity> findUsing(UserName userName) {
        return Optional.of((UserEntity) session().createCriteria(DefaultUserEntity.class).add(eq("userName", userName.getName())).uniqueResult());
    }

    @Override public void save(UserEntity userEntity) {
        session().save(userEntity);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
