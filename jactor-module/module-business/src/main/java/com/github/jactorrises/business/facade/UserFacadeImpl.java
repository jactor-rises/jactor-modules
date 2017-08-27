package com.github.jactorrises.business.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.client.dao.UserDao;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDao userDao;

    public UserFacadeImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        Optional<UserEntity> userEntityOptional = userDao.findUsing(userName);

        if (!userEntityOptional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(userEntityOptional.get());
    }
}
