package com.gitlab.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntryDto")
class BlogEntryDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setBlog(new BlogDto());
        blogEntryDto.setCreationTime(LocalDateTime.now());
        blogEntryDto.setCreatorName("someone");
        blogEntryDto.setEntry("entry");

        BlogEntryDto copied = new BlogEntryDto(
                blogEntryDto.asPersistentDto(), blogEntryDto.getBlog(), blogEntryDto.getCreatorName(), blogEntryDto.getEntry()
        );

        assertAll(
                () -> assertThat(copied.getBlog()).as("blog").isEqualTo(blogEntryDto.getBlog()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(blogEntryDto.getCreationTime()),
                () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(blogEntryDto.getCreatorName()),
                () -> assertThat(copied.getEntry()).as("entry").isEqualTo(blogEntryDto.getEntry())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setCreatedBy("jactor");
        blogEntryDto.setCreationTime(LocalDateTime.now());
        blogEntryDto.setId(1L);
        blogEntryDto.setUpdatedBy("tip");
        blogEntryDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new BlogEntryDto(
                blogEntryDto.asPersistentDto(), null, null, null
        ).asPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(blogEntryDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(blogEntryDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(blogEntryDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(blogEntryDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(blogEntryDto.getUpdatedTime())
        );
    }
}
