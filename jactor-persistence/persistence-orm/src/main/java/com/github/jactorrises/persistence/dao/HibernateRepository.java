package com.github.jactorrises.persistence.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.client.dao.PersistentDao;
import com.github.jactorrises.persistence.client.dto.UserDto;
import com.github.jactorrises.persistence.entity.user.UserNameEmbeddable;
import com.github.jactorrises.persistence.entity.user.UserOrm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public class HibernateRepository implements PersistentDao {

    private final SessionFactory sessionFactory;

    @Autowired public HibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override public Optional<UserDto> findUsing(UserName userName) {
        UserOrm userEntity = (UserOrm) session().createCriteria(UserOrm.class)
                .add(Restrictions.eq("userName", new UserNameEmbeddable(userName)))
                .uniqueResult();

        return Optional.ofNullable(userEntity).map(UserOrm::asDto);
    }

    @Override public <T extends Persistent<?>> T saveOrUpdate(T entity) {
        session().saveOrUpdate(entity);
        return entity;
    }

    @Override public <T extends Persistent<I>, I extends Serializable> T fetch(Class<T> entityClass, I id) {
        return session().load(entityClass, id);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

}
