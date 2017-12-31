package com.github.jactorrises.model.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.dao.PersistentDao;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final PersistentDao persistentDao;

    public UserFacadeImpl(PersistentDao persistentDao) {
        this.persistentDao = persistentDao;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        return persistentDao.findUsing(userName)
                .map(UserDomain::new);
    }
}
