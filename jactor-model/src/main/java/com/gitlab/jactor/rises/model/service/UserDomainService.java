package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.datatype.Username;
import com.gitlab.jactor.rises.commons.dto.UserDto;
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

    Optional<UserDto> fetch(Long id) {
        return userRestService.fetch(id);
    }

    public Optional<UserDto> find(Username username) {
        return userRestService.find(username);
    }

    public List<String> findAllUsernames() {
        return userRestService.findAllUsernames();
    }
}
