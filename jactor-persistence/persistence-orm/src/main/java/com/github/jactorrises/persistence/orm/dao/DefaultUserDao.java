package nu.hjemme.persistence.orm.dao;

import nu.hjemme.client.datatype.UserName;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.client.dao.UserDao;
import nu.hjemme.persistence.orm.domain.DefaultUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

import static org.hibernate.criterion.Restrictions.eq;

public class DefaultUserDao implements UserDao {

    private final SessionFactory sessionFactory;

    public DefaultUserDao(SessionFactory sessionFactory) {
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
