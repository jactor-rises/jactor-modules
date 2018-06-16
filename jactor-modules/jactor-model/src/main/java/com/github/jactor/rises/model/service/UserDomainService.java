package com.github.jactor.rises.model.service;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.model.domain.user.UserDomain;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDomainService {
    private final UserRestService userRestService;

    public UserDomainService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    public UserDomain saveOrUpdate(UserDomain userDomain) {
        return new UserDomain(userRestService.saveOrUpdate(userDomain.getDto()));
    }

    @SuppressWarnings("unchecked") public <T extends User> Optional<T> ferch(Long id) {
        return userRestService.fetch(id).map(UserDomain::new).map(userDomain -> (T) userDomain);
    }

    @SuppressWarnings("unchecked") public <T extends User> Optional<T> find(Username username) {
        return userRestService.find(username).map(UserDomain::new).map(userDomain -> (T) userDomain);
    }
}
