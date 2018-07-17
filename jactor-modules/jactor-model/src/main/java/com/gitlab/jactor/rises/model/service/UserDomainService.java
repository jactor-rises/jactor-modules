package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.client.datatype.Username;
import com.gitlab.jactor.rises.client.domain.User;
import com.gitlab.jactor.rises.io.rest.UserRestService;
import com.gitlab.jactor.rises.model.domain.user.UserDomain;

import java.util.List;
import java.util.Optional;

public class UserDomainService {
    private final UserRestService userRestService;

    public UserDomainService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    public UserDomain saveOrUpdate(UserDomain userDomain) {
        return new UserDomain(userRestService.saveOrUpdate(userDomain.getDto()));
    }

    @SuppressWarnings("unchecked")
    public <T extends User> Optional<T> ferch(Long id) {
        return userRestService.fetch(id).map(UserDomain::new).map(userDomain -> (T) userDomain);
    }

    @SuppressWarnings("unchecked")
    public <T extends User> Optional<T> find(Username username) {
        return userRestService.find(username).map(UserDomain::new).map(userDomain -> (T) userDomain);
    }

    public List<String> findAllUsernames() {
        return userRestService.findAllUsernames();
    }
}
