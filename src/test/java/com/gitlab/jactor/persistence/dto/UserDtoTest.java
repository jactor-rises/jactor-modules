package com.gitlab.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A UserDto")
class UserDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        UserDto userDto = new UserDto();
        userDto.setEmailAddress("somewhere@time");
        userDto.setPerson(new PersonDto());
        userDto.setUsername("me");

        UserDto copied = new UserDto(
                userDto.asPersistentDto(), userDto.getPerson(), userDto.getEmailAddress(), userDto.getUsername()
        );

        assertAll(
                () -> assertThat(copied.getEmailAddress()).as("email address").isEqualTo(userDto.getEmailAddress()),
                () -> assertThat(copied.getPerson()).as("person").isEqualTo(userDto.getPerson()),
                () -> assertThat(copied.getUsername()).as("user name").isEqualTo(userDto.getUsername())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        UserDto userDto = new UserDto();
        userDto.setCreatedBy("jactor");
        userDto.setCreationTime(LocalDateTime.now());
        userDto.setId(1L);
        userDto.setUpdatedBy("tip");
        userDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new UserDto(
                userDto.asPersistentDto(), null, null, null
        ).asPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(userDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(userDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(userDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(userDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(userDto.getUpdatedTime())
        );
    }

}
