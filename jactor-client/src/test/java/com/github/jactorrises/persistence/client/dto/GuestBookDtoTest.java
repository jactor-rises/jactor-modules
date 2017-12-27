package com.github.jactorrises.persistence.client.dto;

import com.github.jactorrises.client.datatype.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookDto")
class GuestBookDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setTitle("title");
        guestBookDto.setUser(new UserDto());

        GuestBookDto copied = new GuestBookDto(guestBookDto);

        assertAll(
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(guestBookDto.getTitle()),
                () -> assertThat(copied.getUser()).as("title").isEqualTo(guestBookDto.getUser())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        GuestBookDto persistentDto = new GuestBookDto();
        persistentDto.setCreatedBy(new Name("jactor"));
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy(new Name("tip"));
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new GuestBookDto(persistentDto);

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualByComparingTo(persistentDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}