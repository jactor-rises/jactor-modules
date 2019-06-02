package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.command.CreateUserCommand;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("An itegrated UserController")
@Transactional
class UserControllerIntegrationTest {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;

  @Test
  @DisplayName("should create a new  user")
  void shouldCreateNewUser() {
    var createUserCommand = new CreateUserCommand("turbo", "Someone");
    var createdUserRespnse = Optional.ofNullable(testRestTemplate.postForEntity(url(
        "user/create"), new HttpEntity<>(createUserCommand), Long.class)
    );

    assertThat(createdUserRespnse).hasValueSatisfying(response -> assertAll(
        () -> assertThat(response.getStatusCode()).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(response.getBody()).as("primary key").isNotNull()
    ));
  }

  private String url(String relativePath) {
    return "http://localhost:" + port + "/jactor-persistence/" + relativePath;
  }
}
