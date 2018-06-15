package com.github.jactor.rises.model.service;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.model.domain.user.UserDomain;
import com.github.jactor.rises.model.service.rest.UserRestService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDomainService {
    private final UserRestService userRestService;

    public UserDomainService(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    public UserDomain saveOrUpdateUser(UserDomain userDomain) {
        return new UserDomain(userRestService.saveOrUpdate(userDomain.getDto()));
    }

    @SuppressWarnings("unchecked") public <T extends User> Optional<T> find(Username username) {
        return userRestService.find(username).map(userDomain -> (T) userDomain);
    }
}
