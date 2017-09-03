package com.github.jactorrises.persistence.repository;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.entity.user.UserEntity;
import com.github.jactorrises.persistence.client.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class HibernateRepository implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override public Optional<UserEntity> findUsing(UserName userName) {
        UserEntity userEntity = (UserEntity) session().createCriteria(UserEntity.class)
                .add(Restrictions.eq("userName", userName.getName()))
                .uniqueResult();

        return Optional.ofNullable(userEntity);
    }

    @Override public void save(UserEntity userEntity) {
        saveOrUpdate(userEntity);
    }

    public <T extends Persistent<?>> T saveOrUpdate(T entity) {
        session().saveOrUpdate(entity);
        return entity;
    }

    public <T extends Persistent<I>, I extends Serializable> T load(Class<T> entityClass, I id) {
        return session().load(entityClass, id);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
