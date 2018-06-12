package com.github.jactor.rises.model.service;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.util.Optional;

public class UserDomainService {
    private final UserRestService userRestService;

    public UserDomainService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    public UserDomain saveOrUpdateUser(UserDomain userDomain) {
        return new UserDomain(userRestService.saveOrUpdate(userDomain.getDto()));
    }

    public Optional<UserDomain> find(UserName userName) {
        return userRestService.find(userName).map(UserDomain::new);
    }
}
