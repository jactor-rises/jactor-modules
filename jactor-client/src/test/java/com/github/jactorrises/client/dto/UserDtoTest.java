package com.github.jactorrises.client.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A UserDto")
class UserDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        UserDto userDto = new UserDto();
        userDto.setBlog(new BlogDto());
        userDto.setGuestBook(new GuestBookDto());
        userDto.setEmailAddress("somewhere@time");
        userDto.setPerson(new PersonDto());
        userDto.setUserName("me");

        UserDto copied = new UserDto(userDto);

        assertAll(
                () -> assertThat(copied.getBlog()).as("blog").isEqualTo(userDto.getBlog()),
                () -> assertThat(copied.getGuestBook()).as("guest book").isEqualTo(userDto.getGuestBook()),
                () -> assertThat(copied.getEmailAddress()).as("email address").isEqualTo(userDto.getEmailAddress()),
                () -> assertThat(copied.getPerson()).as("person").isEqualTo(userDto.getPerson()),
                () -> assertThat(copied.getUserName()).as("user name").isEqualTo(userDto.getUserName())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        UserDto persistentDto = new UserDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy("tip");
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new UserDto(persistentDto);

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }

}
