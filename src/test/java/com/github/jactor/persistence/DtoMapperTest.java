package com.github.jactor.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.dto.Usertype;
import com.github.jactor.shared.dto.UserDto;
import com.github.jactor.shared.dto.UserType;
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
    UserDto userlDto = new UserDto();
    userlDto.setId(1L);
    userlDto.setEmailAddress("some@where");
    userlDto.setUsername("mine");
    userlDto.setUserType(UserType.ACTIVE);

    assertAll(
        () -> assertThat(objectMapper.writeValueAsString(userlDto)).as("id").contains("\"id\":1"),
        () -> assertThat(objectMapper.writeValueAsString(userlDto)).as("email address").contains("\"emailAddress\":\"some@where\""),
        () -> assertThat(objectMapper.writeValueAsString(userlDto)).as("username").contains("\"username\":\"mine\""),
        () -> assertThat(objectMapper.writeValueAsString(userlDto)).as("user type").contains("\"userType\":\"ACTIVE\"")
    );
  }
}
