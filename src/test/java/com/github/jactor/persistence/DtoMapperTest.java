package com.github.jactor.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.dto.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("An ObjectMapper in jactor persistence")
@SpringBootTest(classes = JactorPersistence.class)
class DtoMapperTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("should map a user to json")
  void shouldMapUserToJson() {
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setEmailAddress("some@where");
    userDto.setUsername("mine");
    userDto.setUserType(UserType.ACTIVE);

    assertAll(
        () -> assertThat(objectMapper.writeValueAsString(userDto)).as("id").contains("\"id\":1"),
        () -> assertThat(objectMapper.writeValueAsString(userDto)).as("email address").contains("\"emailAddress\":\"some@where\""),
        () -> assertThat(objectMapper.writeValueAsString(userDto)).as("username").contains("\"username\":\"mine\""),
        () -> assertThat(objectMapper.writeValueAsString(userDto)).as("user type").contains("\"userType\":\"ACTIVE\"")
    );
  }
}
