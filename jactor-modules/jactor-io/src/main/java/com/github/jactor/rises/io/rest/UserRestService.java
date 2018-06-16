package com.github.jactor.rises.io.rest;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Optional;

public class UserRestService extends AbstractRestService {

    public UserRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate, baseUrl);
    }


    public UserDto saveOrUpdate(UserDto userDto) {
        ResponseEntity<UserDto> responseEntity = exchangePost(
                fullUrl("/user/persist"), new HttpEntity<>(userDto), UserDto.class
        );

        return bodyOf(responseEntity);
    }

    public Optional<UserDto> fetch(Serializable id) {
        ResponseEntity<UserDto> responseEntity = getForEntity(fullUrl("/user/get/" + id), UserDto.class);

        return Optional.ofNullable(bodyOf(responseEntity));
    }

    public Optional<UserDto> find(Username username) {
        ResponseEntity<UserDto> responseEntity = getForEntity(fullUrl("/user/find/" + username.asString()), UserDto.class);

        return Optional.ofNullable(bodyOf(responseEntity));
    }
}
