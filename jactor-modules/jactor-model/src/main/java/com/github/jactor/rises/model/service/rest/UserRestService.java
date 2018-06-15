package com.github.jactor.rises.model.service.rest;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.model.domain.user.UserDomain;
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

    public Optional<User> find(Username username) {
        ResponseEntity<UserDto> responseEntity = getForEntity(baseUrl + "/find/" + username.asString(), UserDto.class);

        return Optional.ofNullable(bodyOf(responseEntity)).map(UserDomain::new);
    }
}
