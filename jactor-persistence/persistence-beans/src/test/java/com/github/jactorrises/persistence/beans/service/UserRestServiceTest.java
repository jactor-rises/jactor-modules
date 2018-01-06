package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A UserRestService")
class UserRestServiceTest {

    private UserRestService userRestService;

    @Mock
    private RestTemplate restTemplateMock;

    @BeforeEach
    void initAndMock() {
        MockitoAnnotations.initMocks(this);
        userRestService = new UserRestService(restTemplateMock, "/user");
    }

    @DisplayName("should use RestTemplate GET to find a user")
    @Test void shouldUseRestTemplateGetToFindUser() {
        when(restTemplateMock.getForEntity(anyString(), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        userRestService.find(new UserName("jactor"));

        verify(restTemplateMock).getForEntity(eq("/user/jactor"), eq(UserDto.class));
    }

    @DisplayName("should use RestTemplate GET to fetch a user")
    @Test void shouldUseRestTemplateGetToFetchUser() {
        when(restTemplateMock.getForEntity(anyString(), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        userRestService.fetch(1L);

        verify(restTemplateMock).getForEntity(eq("/user/1"), eq(UserDto.class));
    }

    @DisplayName("should exchange user values with HttpMethod.POST when saving")
    @Test void shouldExchangeUserValuesWithHttpPostWhenSaveOrUpdate() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        UserDto userDto = new UserDto();
        userRestService.saveOrUpdate(userDto);

        verify(restTemplateMock).exchange(
                eq("/user/save"), eq(HttpMethod.POST), eq(new HttpEntity<>(userDto)), eq(UserDto.class)
        );
    }

    @DisplayName("should update user values with HttpMethod.PUT")
    @Test void shouldUpdateUserValuesWithHttpPut() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userRestService.saveOrUpdate(userDto);

        verify(restTemplateMock).put(eq("/user/update"), eq(userDto));
    }
}
