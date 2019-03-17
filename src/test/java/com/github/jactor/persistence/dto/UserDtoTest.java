package com.github.jactor.persistence.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A UserDto")
class UserDtoTest {

  @DisplayName("should have a copy constructor")
  @Test
  void shouldHaveCopyConstructor() {
    UserDto userDto = new UserDto();
    userDto.setEmailAddress("somewhere@time");
    userDto.setPerson(new PersonDto());
    userDto.setUsername("me");

    UserDto copied = new UserDto(userDto.fetchPersistentDto(), userDto);

    assertAll(
        () -> assertThat(copied.getEmailAddress()).as("email address").isEqualTo(userDto.getEmailAddress()),
        () -> assertThat(copied.getPerson()).as("person").isEqualTo(userDto.getPerson()),
        () -> assertThat(copied.getUsername()).as("user name").isEqualTo(userDto.getUsername())
    );
  }

  @DisplayName("should give values to PersistentDto")
  @Test
  void shouldGiveValuesToPersistentDto() {
    PersistentDto persistentDto = new PersistentDto();
    persistentDto.setCreatedBy("jactor");
    persistentDto.setTimeOfCreation(LocalDateTime.now());
    persistentDto.setId(1L);
    persistentDto.setModifiedBy("tip");
    persistentDto.setTimeOfModification(LocalDateTime.now());

    PersistentDto copied = new UserDto(persistentDto, new UserDto()).fetchPersistentDto();

    assertAll(
        () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
        () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
        () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
        () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
        () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
    );
  }
}
