package com.github.jactorrises.model.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.model.persistence.client.dao.UserDao;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDao userDao;

    public UserFacadeImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        Optional<UserEntity> userEntityOptional = userDao.findUsing(userName);

        return userEntityOptional
                .map(UserDomain::new);
    }
}
