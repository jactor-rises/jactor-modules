package com.github.jactorrises.model.internal.facade;

import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.model.internal.persistence.client.dao.UserDao;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;

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
