package com.github.jactorrises.client.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntryDto")
class GuestBookEntryDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        guestBookEntryDto.setCreatedTime(LocalDateTime.now());
        guestBookEntryDto.setCreatorName("me");
        guestBookEntryDto.setGuestBook(new GuestBookDto());
        guestBookEntryDto.setEntry("entry");

        GuestBookEntryDto copied = new GuestBookEntryDto(guestBookEntryDto);

        assertAll(
                () -> assertThat(copied.getCreatedTime()).as("created time").isEqualTo(guestBookEntryDto.getCreatedTime()),
                () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(guestBookEntryDto.getCreatorName()),
                () -> assertThat(copied.getGuestBook()).as("guest book").isEqualTo(guestBookEntryDto.getGuestBook()),
                () -> assertThat(copied.getEntry()).as("entry").isEqualTo(guestBookEntryDto.getEntry())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        GuestBookEntryDto persistentDto = new GuestBookEntryDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy("tip");
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new GuestBookEntryDto(persistentDto);

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}