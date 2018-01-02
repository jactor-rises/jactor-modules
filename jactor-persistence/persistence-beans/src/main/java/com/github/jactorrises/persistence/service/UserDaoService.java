package com.github.jactorrises.persistence.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;

import java.io.Serializable;
import java.util.Optional;

public class UserDaoService {
    public Serializable saveOrUpdate(UserDto userDto) {
        return userDto.getId();
    }

    public Optional<UserDto> find(UserName userName) {
        UserDto userDto = new UserDto();
        userDto.setUserName(userName.asString());

        return Optional.of(userDto);
    }
}
