package com.github.jactor.persistence.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A BlogEntryDto")
class BlogEntryDtoTest {

  @DisplayName("should have a copy constructor")
  @Test
  void shouldHaveCopyConstructor() {
    BlogEntryDto blogEntryDto = new BlogEntryDto();
    blogEntryDto.setBlog(new BlogDto());
    blogEntryDto.setCreatorName("someone");
    blogEntryDto.setEntry("entry");

    BlogEntryDto copied = new BlogEntryDto(blogEntryDto.fetchPersistentDto(), blogEntryDto);

    assertAll(
        () -> assertThat(copied.getBlog()).as("blog").isEqualTo(blogEntryDto.getBlog()),
        () -> assertThat(copied.getCreatorName()).as("creator name").isEqualTo(blogEntryDto.getCreatorName()),
        () -> assertThat(copied.getEntry()).as("entry").isEqualTo(blogEntryDto.getEntry())
    );
  }

  @DisplayName("should give values to PersistentDto")
  @Test
  void shouldGiveValuesToPersistentDto() {
    PersistentDto persistentDto = new PersistentDto();
    persistentDto.setCreatedBy("jactor");
    persistentDto.setTimeOfCreation(LocalDateTime.now());
    persistentDto.setId(1L);
    persistentDto.setModifiedBy("tip");
    persistentDto.setTimeOfModification(LocalDateTime.now());

    PersistentDto copied = new BlogEntryDto(persistentDto, new BlogEntryDto()).fetchPersistentDto();

    assertAll(
        () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
        () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
        () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
        () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
        () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
    );
  }
}
