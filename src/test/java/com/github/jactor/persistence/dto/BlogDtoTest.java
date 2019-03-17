package com.github.jactor.persistence.dto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A BlogDto")
class BlogDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        BlogDto blogDto = new BlogDto();
        blogDto.setCreated(LocalDate.now());
        blogDto.setTitle("title");
        blogDto.setUser(new UserDto());

        BlogDto copied = new BlogDto(blogDto.fetchPersistentDto(), blogDto);

        assertAll(
                () -> assertThat(copied.getCreated()).as("created").isEqualTo(blogDto.getCreated()),
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(blogDto.getTitle()),
                () -> assertThat(copied.getUser()).as("user").isEqualTo(blogDto.getUser())
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

        PersistentDto copied = new BlogDto(persistentDto, new BlogDto()).fetchPersistentDto();

        assertAll(
            () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
            () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
            () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
            () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
            () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
