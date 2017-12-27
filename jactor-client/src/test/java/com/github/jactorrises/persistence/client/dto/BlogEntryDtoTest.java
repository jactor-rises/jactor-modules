package com.github.jactorrises.persistence.client.dto;

import com.github.jactorrises.client.datatype.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntryDto")
class BlogEntryDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setBlog(new BlogDto());
        blogEntryDto.setCreatedTime(LocalDateTime.now());
        blogEntryDto.setCreatorName(new Name("someone"));
        blogEntryDto.setEntry("entry");

        BlogEntryDto copied = new BlogEntryDto(blogEntryDto);

        assertAll(
                () -> assertThat(copied.getBlog()).as("blog").isEqualTo(blogEntryDto.getBlog()),
                () -> assertThat(copied.getCreatedTime()).as("created time").isEqualTo(blogEntryDto.getCreatedTime()),
                () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(blogEntryDto.getCreatorName()),
                () -> assertThat(copied.getEntry()).as("entry").isEqualTo(blogEntryDto.getEntry())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        BlogEntryDto persistentDto = new BlogEntryDto();
        persistentDto.setCreatedBy(new Name("jactor"));
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy(new Name("tip"));
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new BlogEntryDto(persistentDto);

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualByComparingTo(persistentDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}