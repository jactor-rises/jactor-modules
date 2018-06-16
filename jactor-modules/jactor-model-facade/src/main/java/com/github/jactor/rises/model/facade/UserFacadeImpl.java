package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.model.service.UserDomainService;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDomainService userDomainService;

    UserFacadeImpl(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    @Override public Optional<User> fetch(Long id) {
        return userDomainService.ferch(id);
    }

    @Override public Optional<User> find(Username username) {
        return userDomainService.find(username);
    }
}
