package com.gitlab.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntryDto")
class GuestBookEntryDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        guestBookEntryDto.setCreationTime(LocalDateTime.now());
        guestBookEntryDto.setCreatorName("me");
        guestBookEntryDto.setGuestBook(new GuestBookDto());
        guestBookEntryDto.setEntry("entry");

        GuestBookEntryDto copied = new GuestBookEntryDto(
                guestBookEntryDto.asPersistentDto(), guestBookEntryDto.getGuestBook(), guestBookEntryDto.getCreatorName(), guestBookEntryDto.getEntry()
        );

        assertAll(
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(guestBookEntryDto.getCreationTime()),
                () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(guestBookEntryDto.getCreatorName()),
                () -> assertThat(copied.getGuestBook()).as("guest book").isEqualTo(guestBookEntryDto.getGuestBook()),
                () -> assertThat(copied.getEntry()).as("entry").isEqualTo(guestBookEntryDto.getEntry())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        guestBookEntryDto.setCreatedBy("jactor");
        guestBookEntryDto.setCreationTime(LocalDateTime.now());
        guestBookEntryDto.setId(1L);
        guestBookEntryDto.setUpdatedBy("tip");
        guestBookEntryDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new GuestBookEntryDto(
                guestBookEntryDto.asPersistentDto(), null, null, null
        ).asPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(guestBookEntryDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(guestBookEntryDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(guestBookEntryDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(guestBookEntryDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(guestBookEntryDto.getUpdatedTime())
        );
    }
}
