package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.service.UserDaoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDomainService {
    private final UserDaoService userDaoService;

    public UserDomainService(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    public UserDomain saveOrUpdateUser(UserDomain userDomain) {
        userDomain.setId(userDaoService.saveOrUpdate(userDomain.getDto()));
        return userDomain;
    }

    public Optional<UserDomain> find(UserName userName) {
        return userDaoService.find(userName).map(UserDomain::new);
    }
}
