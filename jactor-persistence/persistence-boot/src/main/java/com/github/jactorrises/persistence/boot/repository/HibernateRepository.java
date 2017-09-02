package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.UserEntity;
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
        UserEntity userEntity = (UserEntity) session().createCriteria(UserEntityImpl.class)
                .add(eq("userName", userName.getName()))
                .uniqueResult();

        return Optional.ofNullable(userEntity);
    }

    @Override public void save(UserEntity userEntity) {
        saveOrUpdate(userEntity);
    }

    public <T extends Persistent<?>> T saveOrUpdate(T addressEntity) {
        session().saveOrUpdate(addressEntity);
        return addressEntity;
    }

    public <T extends Persistent<I>, I extends Serializable> T load(Class<T> entity, I id) {
        return session().load(entity, id);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
