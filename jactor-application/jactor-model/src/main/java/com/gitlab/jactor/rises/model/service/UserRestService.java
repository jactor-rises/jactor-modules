package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.datatype.Username;
import com.gitlab.jactor.rises.commons.dto.UserDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface UserRestService {
    UserDto saveOrUpdate(UserDto userDto);

    Optional<UserDto> fetch(Serializable id);

    Optional<UserDto> find(Username username);

    List<String> findAllUsernames();
}
