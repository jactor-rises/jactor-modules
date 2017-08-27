package com.github.jactorrises.business.domain.dao;

import com.github.jactorrises.business.domain.UserDomain;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.client.dao.UserDao;

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

        return userDao.findUsing(userName).map(UserDomain::new);
    }

    void save(UserDomain userDomain) {
        if (userDomain != null) {
            userDao.save(userDomain.getEntity());
        }
    }
}