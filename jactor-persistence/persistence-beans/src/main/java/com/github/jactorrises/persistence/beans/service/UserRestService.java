package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Optional;

public class UserRestService extends AbstractRestService {

    private final String baseUrl;

    public UserRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }


    public UserDto saveOrUpdate(UserDto userDto) {
        if (userDto.getId() == null) {
            return save(userDto);
        }

        put(baseUrl + "/update", userDto);

        return userDto;
    }

    private UserDto save(UserDto userDto) {
        ResponseEntity<UserDto> responseEntity = exchange(
                baseUrl + "/save", HttpMethod.POST, new HttpEntity<>(userDto), UserDto.class
        );

        return bodyOf(responseEntity);
    }

    public UserDto fetch(Serializable id) {
        ResponseEntity<UserDto> responseEntity = getForEntity(baseUrl + '/' + id, UserDto.class);

        return bodyOf(responseEntity);
    }

    public Optional<UserDto> find(UserName userName) {
        ResponseEntity<UserDto> responseEntity = getForEntity(baseUrl + '/' + userName.asString(), UserDto.class);

        return Optional.ofNullable(bodyOf(responseEntity));
    }
}
