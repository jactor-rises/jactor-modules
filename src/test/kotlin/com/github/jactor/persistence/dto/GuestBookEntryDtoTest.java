package com.github.jactor.persistence.dto;

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
        guestBookEntryDto.setCreatorName("me");
        guestBookEntryDto.setGuestBook(new GuestBookDto());
        guestBookEntryDto.setEntry("entry");

        GuestBookEntryDto copied = new GuestBookEntryDto(guestBookEntryDto.getPersistentDto(), guestBookEntryDto);

        assertAll(
                () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(guestBookEntryDto.getCreatorName()),
                () -> assertThat(copied.getGuestBook()).as("guest book").isEqualTo(guestBookEntryDto.getGuestBook()),
                () -> assertThat(copied.getEntry()).as("entry").isEqualTo(guestBookEntryDto.getEntry())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        PersistentDto persistentDto = new PersistentDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setTimeOfCreation(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setModifiedBy("tip");
        persistentDto.setTimeOfModification(LocalDateTime.now());

        PersistentDto copied = new GuestBookEntryDto(persistentDto, new GuestBookEntryDto()).getPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
                () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
