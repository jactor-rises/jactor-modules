package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("A UserController")
class UserControllerTest {

  @LocalServerPort
  private int port;
  @Value("${server.servlet.context-path}")
  private String contextPath;

  @MockBean
  private UserService userServiceMock;
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  @DisplayName("should build full path")
  void shouldBuildFullPath() {
    assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:" + port + "/jactor-persistence/somewhere");
  }

  @Test
  @DisplayName("should not find a user by username")
  void shouldNotFindByUsername() {
    when(userServiceMock.find("me")).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by username")
  void shouldFindByUsername() {
    when(userServiceMock.find("me")).thenReturn(Optional.of(new UserInternalDto()));

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should not get a user by id")
  void shouldNotGetById() {
    when(userServiceMock.find(1L)).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/id/1"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NOT_FOUND),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by id")
  void shouldFindById() {
    when(userServiceMock.find(1L)).thenReturn(Optional.of(new UserInternalDto()));

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/id/1"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should modify existing user")
  void shouldModifyExistingUser() {
    UserInternalDto userInternalDto = new UserInternalDto();
    userInternalDto.setId(1L);

    when(userServiceMock.update(any(UserInternalDto.class))).thenReturn(userInternalDto);

    var userRespnse = testRestTemplate.exchange(buildFullPath("/user/id/1"), HttpMethod.PUT, new HttpEntity<>(userInternalDto), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull(),
        () -> assertThat(userRespnse.getBody()).extracting(UserInternalDto::getId).as("user id").isEqualTo(1L),
        () -> verify(userServiceMock).update(any(UserInternalDto.class))
    );
  }

  @Test
  @DisplayName("should find all usernames on active users")
  void shouldFindAllUsernames() {
    when(userServiceMock.findUsernamesOnActiveUsers())
        .thenReturn(List.of("bart", "lisa"));

    var userRespnse = testRestTemplate.exchange(buildFullPath("/user/active/usernames"), HttpMethod.GET, null, responsIslistOfStrings());

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse.getBody()).as("user").isEqualTo(List.of("bart", "lisa"))
    );
  }

  @Test
  @DisplayName("should add user id from url to payload when updating user")
  void shouldAddUserIdFromPathWhenUpdatingUser() {
    when(userServiceMock.update(any(UserInternalDto.class))).thenReturn(new UserInternalDto());

    var userResponse = testRestTemplate.exchange(buildFullPath("/user/id/101"), HttpMethod.PUT, new HttpEntity<>(new UserInternalDto()), UserInternalDto.class);

    assertAll(
        () -> assertThat(userResponse.getStatusCode()).as("status code").isEqualTo(HttpStatus.ACCEPTED),
        () -> {
          var userDto = new UserInternalDto();
          userDto.setId(101L);

          verify(userServiceMock).update(userDto);
        }
    );
  }

  private String buildFullPath(String url) {
    return "http://localhost:" + port + contextPath + url;
  }

  private ParameterizedTypeReference<List<String>> responsIslistOfStrings() {
    return new ParameterizedTypeReference<>() {
    };
  }
}
