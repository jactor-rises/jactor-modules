package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.UserEntity;
import com.github.jactor.persistence.entity.UserEntity.UserType;
import com.github.jactor.persistence.repository.UserRepository;
import com.github.jactor.shared.dto.CreateUserCommandDto;
import com.github.jactor.shared.dto.PersonDto;
import com.github.jactor.shared.dto.UserDto;
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
  private UserRepository userRepositoryMock;
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
    when(userRepositoryMock.findByUsername("me")).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by username")
  void shouldFindByUsername() {
    when(userRepositoryMock.findByUsername("me")).thenReturn(Optional.of(new UserEntity(new UserInternalDto())));

    var userResponse = testRestTemplate.getForEntity(buildFullPath("/user/name/me"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userResponse).extracting(ResponseEntity::getBody).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should not get a user by id")
  void shouldNotGetById() {
    when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/1"), UserInternalDto.class);

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NOT_FOUND),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user").isNull()
    );
  }

  @Test
  @DisplayName("should find a user by id")
  void shouldFindById() {
    when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(new UserEntity(new UserInternalDto())));

    var userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/1"), UserInternalDto.class);

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

    when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(new UserEntity(userInternalDto)));

    var userRespnse = testRestTemplate.exchange(
        buildFullPath("/user/1"), HttpMethod.PUT, new HttpEntity<>(userInternalDto.toUserDto()), UserDto.class
    );

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(userRespnse).extracting(ResponseEntity::getBody).as("user")
            .isNotNull().extracting(UserDto::getId).as("user id").isEqualTo(1L)
    );
  }

  @Test
  @DisplayName("should find all usernames on active users")
  void shouldFindAllUsernames() {
    UserDto bartDto = new UserDto(null, null, new PersonDto(), "bart", com.github.jactor.shared.dto.UserType.ACTIVE);
    UserDto lisaDto = new UserDto(null, null, new PersonDto(), "lisa", com.github.jactor.shared.dto.UserType.ACTIVE);
    UserEntity bart = new UserEntity(new UserInternalDto(bartDto));
    UserEntity lisa = new UserEntity(new UserInternalDto(lisaDto));

    when(userRepositoryMock.findByUserTypeIn(List.of(UserType.ACTIVE)))
        .thenReturn(List.of(bart, lisa));

    var userRespnse = testRestTemplate.exchange(buildFullPath("/user/usernames"), HttpMethod.GET, null, responsIslistOfStrings());

    assertAll(
        () -> assertThat(userRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(userRespnse.getBody()).as("user").isEqualTo(List.of("bart", "lisa"))
    );
  }

  @Test
  @DisplayName("should accept if user id is valid")
  void shouldAcceptValidUserId() {
    when(userRepositoryMock.findById(101L)).thenReturn(Optional.of(new UserEntity(new UserInternalDto())));

    var userResponse = testRestTemplate.exchange(buildFullPath("/user/101"), HttpMethod.PUT, new HttpEntity<>(new UserDto()), UserDto.class);

    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
  }

  @Test
  @DisplayName("should not accept if user id is invalid")
  void shouldNotAcceptInvalidUserId() {
    when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());

    var userResponse = testRestTemplate.exchange(buildFullPath("/user/101"), HttpMethod.PUT, new HttpEntity<>(new UserDto()), UserDto.class);

    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private String buildFullPath(String url) {
    return "http://localhost:" + port + contextPath + url;
  }

  private ParameterizedTypeReference<List<String>> responsIslistOfStrings() {
    return new ParameterizedTypeReference<>() {
    };
  }

  @Test
  @DisplayName("should return BAD_REQUEST when username is occupied")
  void shouldReturnBadRequestWhenUsernameIsOccupied() {
    when(userRepositoryMock.findByUsername("turbo")).thenReturn(Optional.of(new UserEntity()));

    CreateUserCommandDto createUserCommand = new CreateUserCommandDto();
    createUserCommand.setUsername("turbo");
    var userResponse = testRestTemplate.exchange(buildFullPath("/user"), HttpMethod.POST, new HttpEntity<>(createUserCommand), UserDto.class);

    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
