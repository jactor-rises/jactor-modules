package com.gitlab.jactor.rises.model.facade.impl;

import com.gitlab.jactor.rises.client.datatype.Username;
import com.gitlab.jactor.rises.client.domain.User;
import com.gitlab.jactor.rises.client.facade.UserFacade;
import com.gitlab.jactor.rises.model.service.UserDomainService;

import java.util.List;
import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDomainService userDomainService;

    public UserFacadeImpl(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public @Override Optional<User> fetch(Long id) {
        return userDomainService.ferch(id);
    }

    public @Override Optional<User> find(Username username) {
        return userDomainService.find(username);
    }

    public @Override List<String> findAllUsernames() {
        return userDomainService.findAllUsernames();
    }
}
