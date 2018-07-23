package com.gitlab.jactor.rises.model.facade.impl;

import com.gitlab.jactor.rises.commons.datatype.Username;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.model.facade.UserFacade;
import com.gitlab.jactor.rises.model.service.UserDomainService;

import java.util.List;
import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDomainService userDomainService;

    public UserFacadeImpl(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public @Override Optional<UserDto> fetch(Long id) {
        return userDomainService.fetch(id);
    }

    public @Override Optional<UserDto> find(Username username) {
        return userDomainService.find(username);
    }

    public @Override List<String> findAllUsernames() {
        return userDomainService.findAllUsernames();
    }
}
