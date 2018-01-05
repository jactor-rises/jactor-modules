package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class UserDaoService {
    private final RestTemplate restTemplate;

    public UserDaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto saveOrUpdate(UserDto userDto) {
        return userDto;
    }

    public Optional<UserDto> find(UserName userName) {
        UserDto userDto = new UserDto();
        userDto.setUserName(userName.asString());

        return Optional.of(userDto);
    }
}
