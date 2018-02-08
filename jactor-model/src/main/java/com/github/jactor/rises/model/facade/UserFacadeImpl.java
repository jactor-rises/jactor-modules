package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.model.domain.user.UserDomain;
import com.github.jactor.rises.persistence.beans.service.UserRestService;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserRestService userRestService;

    public UserFacadeImpl(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        return userRestService.find(userName).map(UserDomain::new);
    }
}
