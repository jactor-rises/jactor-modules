package com.github.jactor.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.dto.Usertype;
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
    UserInternalDto userInternalDto = new UserInternalDto();
    userInternalDto.setId(1L);
    userInternalDto.setEmailAddress("some@where");
    userInternalDto.setUsername("mine");
    userInternalDto.setUsertype(Usertype.ACTIVE);

    assertAll(
        () -> assertThat(objectMapper.writeValueAsString(userInternalDto)).as("id").contains("\"id\":1"),
        () -> assertThat(objectMapper.writeValueAsString(userInternalDto)).as("email address").contains("\"emailAddress\":\"some@where\""),
        () -> assertThat(objectMapper.writeValueAsString(userInternalDto)).as("username").contains("\"username\":\"mine\""),
        () -> assertThat(objectMapper.writeValueAsString(userInternalDto)).as("user type").contains("\"userType\":\"ACTIVE\"")
    );
  }
}
