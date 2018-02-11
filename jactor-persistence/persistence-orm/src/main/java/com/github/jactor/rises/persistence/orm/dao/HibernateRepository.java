package com.github.jactor.rises.persistence.orm.dao;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.persistence.orm.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public class HibernateRepository {

    private final SessionFactory sessionFactory;

    @Autowired public HibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<UserEntity> findUsing(UserName userName) {
        UserEntity userEntity = (UserEntity) session().createCriteria(UserEntity.class)
                .add(Restrictions.eq("userName", userName.asString()))
                .uniqueResult();

        return Optional.ofNullable(userEntity);
    }

    public <T> T saveOrUpdate(T entity) {
        session().saveOrUpdate(entity);
        return entity;
    }

    public <T, I extends Serializable> T fetch(Class<T> entityClass, I id) {
        T entity = session().load(entityClass, id);

        if (entity == null) {
            String errorMessage = String.format("%s is an unknown id for %s", String.valueOf(id), entityClass.getName());
            throw new IllegalStateException(errorMessage);
        }

        return entity;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

}
