package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.beans.service.UserRestService;
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

    public Optional<UserDomain> find(UserName userName) {
        return userRestService.find(userName).map(UserDomain::new);
    }
}
