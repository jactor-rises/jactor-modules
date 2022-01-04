package com.github.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookDto")
class GuestBookDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setEntries(Set.of(new GuestBookEntryDto()));
        guestBookDto.setTitle("title");
        guestBookDto.setUserInternal(new UserInternalDto());

        GuestBookDto copied = new GuestBookDto(guestBookDto.getPersistentDto(), guestBookDto);

        assertAll(
                () -> assertThat(copied.getEntries()).as("entries").isEqualTo(guestBookDto.getEntries()),
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(guestBookDto.getTitle()),
                () -> assertThat(copied.getUserInternal()).as("title").isEqualTo(guestBookDto.getUserInternal())
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

        PersistentDto copied = new GuestBookDto(persistentDto, new GuestBookDto()).getPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
                () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
