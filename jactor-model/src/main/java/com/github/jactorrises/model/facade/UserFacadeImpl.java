package com.github.jactorrises.model.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.service.UserDaoService;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDaoService userDaoService;

    public UserFacadeImpl(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        return userDaoService.find(userName).map(UserDomain::new);
    }
}
