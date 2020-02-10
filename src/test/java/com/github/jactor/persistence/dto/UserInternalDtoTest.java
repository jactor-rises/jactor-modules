package com.github.jactor.persistence.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A UserDto")
class UserInternalDtoTest {

  @DisplayName("should have a copy constructor")
  @Test
  void shouldHaveCopyConstructor() {
    UserInternalDto userInternalDto = new UserInternalDto();
    userInternalDto.setEmailAddress("somewhere@time");
    userInternalDto.setPerson(new PersonInternalDto());
    userInternalDto.setUsername("me");

    UserInternalDto copied = new UserInternalDto(userInternalDto.fetchPersistentDto(), userInternalDto);

    assertAll(
        () -> assertThat(copied.getEmailAddress()).as("email address").isEqualTo(userInternalDto.getEmailAddress()),
        () -> assertThat(copied.getPerson()).as("person").isEqualTo(userInternalDto.getPerson()),
        () -> assertThat(copied.getUsername()).as("user name").isEqualTo(userInternalDto.getUsername())
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

    PersistentDto copied = new UserInternalDto(persistentDto, new UserInternalDto()).fetchPersistentDto();

    assertAll(
        () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
        () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
        () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
        () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
        () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
    );
  }
}
