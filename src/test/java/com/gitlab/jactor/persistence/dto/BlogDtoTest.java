package com.gitlab.jactor.persistence.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogDto")
class BlogDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        BlogDto blogDto = new BlogDto();
        blogDto.setCreated(LocalDate.now());
        blogDto.setTitle("title");
        blogDto.setUser(new UserDto());

        BlogDto copied = new BlogDto(
                blogDto.asPersistentDto(), blogDto.getCreated(), blogDto.getTitle(), blogDto.getUser()
        );

        assertAll(
                () -> assertThat(copied.getCreated()).as("created").isEqualTo(blogDto.getCreated()),
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(blogDto.getTitle()),
                () -> assertThat(copied.getUser()).as("user").isEqualTo(blogDto.getUser())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        BlogDto blogDto = new BlogDto();
        blogDto.setCreatedBy("jactor");
        blogDto.setCreationTime(LocalDateTime.now());
        blogDto.setId(1L);
        blogDto.setUpdatedBy("tip");
        blogDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new BlogDto(blogDto.asPersistentDto(), null, null, null).asPersistentDto();

        assertAll(
                () -> Assertions.assertThat(copied.getCreatedBy()).as("created by").isEqualTo(blogDto.getCreatedBy()),
                () -> Assertions.assertThat(copied.getCreationTime()).as("creation time").isEqualTo(blogDto.getCreationTime()),
                () -> Assertions.assertThat(copied.getId()).as("id").isEqualTo(blogDto.getId()),
                () -> Assertions.assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(blogDto.getUpdatedBy()),
                () -> Assertions.assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(blogDto.getUpdatedTime())
        );
    }
}
