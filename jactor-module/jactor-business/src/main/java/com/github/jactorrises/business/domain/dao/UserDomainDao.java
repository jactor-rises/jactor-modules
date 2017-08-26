package nu.hjemme.business.domain.dao;

import nu.hjemme.business.domain.UserDomain;
import nu.hjemme.client.datatype.UserName;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.client.dao.UserDao;

import java.util.Optional;

public class UserDomainDao {
    private final UserDao userDao;

    public UserDomainDao(UserDao userDao) {
        this.userDao = userDao;
    }

    Optional<UserDomain> findUsing(UserName userName) {
        if (userName == null) {
            return Optional.empty();
        }

        Optional<UserEntity> entity = userDao.findUsing(userName);

        if (!entity.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new UserDomain(entity.get()));
    }

    void save(UserDomain userDomain) {
        if (userDomain != null) {
            userDao.save(userDomain.getEntity());
        }
    }
}
