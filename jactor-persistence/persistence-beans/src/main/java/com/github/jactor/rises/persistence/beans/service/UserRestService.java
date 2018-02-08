package com.github.jactor.rises.persistence.beans.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.dto.UserDto;
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
        ResponseEntity<UserDto> responseEntity = exchange(
                baseUrl + "/persist", HttpMethod.POST, new HttpEntity<>(userDto), UserDto.class
        );

        return bodyOf(responseEntity);
    }

    public UserDto fetch(Serializable id) {
        ResponseEntity<UserDto> responseEntity = getForEntity(baseUrl + "/get/" + id, UserDto.class);

        return bodyOf(responseEntity);
    }

    public Optional<UserDto> find(UserName userName) {
        ResponseEntity<UserDto> responseEntity = getForEntity(baseUrl + "/find/" + userName.asString(), UserDto.class);

        return Optional.ofNullable(bodyOf(responseEntity));
    }
}
