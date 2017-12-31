package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.datatype.Name;
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
    @Test
    void shouldHaveCopyConstructor() {
        BlogDto blogDto = new BlogDto();
        blogDto.setCreated(LocalDate.now());
        blogDto.setTitle("title");
        blogDto.setUser(new UserDto());

        BlogDto copied = new BlogDto(blogDto);

        assertAll(
                () -> assertThat(copied.getCreated()).as("created").isEqualTo(blogDto.getCreated()),
                () -> assertThat(copied.getTitle()).as("title").isEqualTo(blogDto.getTitle()),
                () -> assertThat(copied.getUser()).as("user").isEqualTo(blogDto.getUser())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        BlogDto persistentDto = new BlogDto();
        persistentDto.setCreatedBy(new Name("jactor"));
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy(new Name("tip"));
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new BlogDto(persistentDto);

        assertAll(
                () -> Assertions.assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> Assertions.assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> Assertions.assertThat(copied.getId()).as("id").isEqualByComparingTo(persistentDto.getId()),
                () -> Assertions.assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> Assertions.assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}