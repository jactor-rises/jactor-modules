package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.client.domain.Persistent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class HibernateRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
