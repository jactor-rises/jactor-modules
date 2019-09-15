package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.command.CreateUserCommandResponse;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.UniqueUsername;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("An integrated UserController")
@Transactional
class UserControllerIntegrationTest {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;

  @Test
  @DisplayName("should create a new user")
  void shouldCreateNewUser() {
    var createUserCommand = new CreateUserCommand(UniqueUsername.generate("turbo"), "Someone");
    var createdUserResponse = Optional.ofNullable(testRestTemplate.postForEntity(
        contextPath() + "/user/create", new HttpEntity<>(createUserCommand), CreateUserCommandResponse.class)
    );

    assertThat(createdUserResponse).hasValueSatisfying(response -> assertAll(
        () -> assertThat(response.getStatusCode()).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(response.getBody()).extracting(CreateUserCommandResponse::getUserId).as("primary key").isNotNull()
    ));
  }

  @Test
  @DisplayName("should create a new user with an email address")
  void shouldCreateNewUserWithAnEmailAddress() {
    var createUserCommand = new CreateUserCommand(UniqueUsername.generate("turbo"), "Someone");
    createUserCommand.setEmailAddress("somewhere@somehow.com");

    var createdUserResponse = Optional.ofNullable(testRestTemplate.postForEntity(
        contextPath() + "/user/create", new HttpEntity<>(createUserCommand), CreateUserCommandResponse.class)
    );

    assertThat(createdUserResponse).hasValueSatisfying(response -> assertAll(
        () -> assertThat(response.getStatusCode()).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(response.getBody()).extracting(CreateUserCommandResponse::getUserId).as("primary key").isNotNull(),
        () -> {
          var userId = Objects.requireNonNull(response.getBody()).getUserId();
          var userDtoEntity = testRestTemplate.exchange(contextPath() + "/user/id/" + userId, HttpMethod.GET, null, UserDto.class);

          assertThat(userDtoEntity.getBody()).extracting(UserDto::getEmailAddress).as("userDto.emailAddress").isEqualTo("somewhere@somehow.com");
        }
    ));
  }

  private String contextPath() {
    return "http://localhost:" + port + "/jactor-persistence";
  }
}
