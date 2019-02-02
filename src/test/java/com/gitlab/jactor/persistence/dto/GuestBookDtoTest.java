package com.gitlab.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookDto")
class GuestBookDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setEntries(Set.of(new GuestBookEntryDto()));
        guestBookDto.setTitle("title");
        guestBookDto.setUser(new UserDto());

        GuestBookDto copied = new GuestBookDto(
                guestBookDto.asPersistentDto(), guestBookDto.getEntries(), guestBookDto.getTitle(), guestBookDto.getUser()
        );

        assertAll(
                () -> assertThat(copied.getEntries()).as("entries").isEqualTo(guestBookDto.getEntries()),
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(guestBookDto.getTitle()),
                () -> assertThat(copied.getUser()).as("title").isEqualTo(guestBookDto.getUser())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setCreatedBy("jactor");
        guestBookDto.setCreationTime(LocalDateTime.now());
        guestBookDto.setId(1L);
        guestBookDto.setUpdatedBy("tip");
        guestBookDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new GuestBookDto(
                guestBookDto.asPersistentDto(), emptySet(), null, null
        ).asPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(guestBookDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(guestBookDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(guestBookDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(guestBookDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(guestBookDto.getUpdatedTime())
        );
    }
}
