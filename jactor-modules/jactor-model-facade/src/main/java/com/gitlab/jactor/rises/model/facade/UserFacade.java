package com.gitlab.jactor.rises.model.facade;

import com.gitlab.jactor.rises.commons.datatype.Username;
import com.gitlab.jactor.rises.commons.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserFacade {
    Optional<UserDto> fetch(Long id);

    Optional<UserDto> find(Username username);

    List<String> findAllUsernames();
}
