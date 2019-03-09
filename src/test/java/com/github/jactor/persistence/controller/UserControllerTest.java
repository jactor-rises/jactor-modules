package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.UserDto;
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
    assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:" + port + "/jactor-persistence-orm/somewhere");
  }

  @Test
  @DisplayName("should not find a user by username")
  void shouldNotFindByUsername() {
    when(userServiceMock.find("me")).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by username")
  void shouldFindByUsername() {
    when(userServiceMock.find("me")).thenReturn(Optional.of(new UserDto()));

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should not get a user by id")
  void shouldNotGetById() {
    when(userServiceMock.find(1L)).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/1"), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by id")
  void shouldFindById() {
    when(userServiceMock.find(1L)).thenReturn(Optional.of(new UserDto()));

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/1"), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should modify existing user")
  void shouldModifyExistingUser() {
    UserDto userDto = new UserDto();
    userDto.setId(1L);

    when(userServiceMock.saveOrUpdate(any(UserDto.class))).thenReturn(userDto);

    var userRespnse = testRestTemplate.exchange(buildFullPath("/user/1"), HttpMethod.PUT, new HttpEntity<>(userDto), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull(),
        () -> assertThat(userRespnse.getBody()).extracting(UserDto::getId).as("user id").isEqualTo(1L),
        () -> verify(userServiceMock).saveOrUpdate(any(UserDto.class))
    );
  }

  @Test
  @DisplayName("should create a user")
  void shouldCreateGuestUser() {
    UserDto userDto = new UserDto();
    UserDto createdDto = new UserDto();
    createdDto.setId(1L);

    when(userServiceMock.saveOrUpdate(any(UserDto.class))).thenReturn(createdDto);

    var userRespnse = testRestTemplate.exchange(buildFullPath("/user"), HttpMethod.POST, new HttpEntity<>(userDto), UserDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNotNull(),
        () -> assertThat(userRespnse.getBody()).extracting(UserDto::getId).as("user id").isEqualTo(1L),
        () -> verify(userServiceMock).saveOrUpdate(any(UserDto.class))
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

  private String buildFullPath(String url) {
    return "http://localhost:" + port + contextPath + url;
  }

  private ParameterizedTypeReference<List<String>> responsIslistOfStrings() {
    return new ParameterizedTypeReference<>() {
    };
  }

}
