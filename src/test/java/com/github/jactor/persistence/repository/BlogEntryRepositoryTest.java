package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.BlogEntryEntity.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A BlogEntryRepository")
class BlogEntryRepositoryTest {

  @Autowired
  private BlogEntryRepository blogEntryRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should save then read blog entry")
  void shouldSaveThenReadBlogEntry() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "white");
    var blogDto = new BlogDto(new PersistentDto(), LocalDate.now(), "and then some...", userDto);
    var blogEntryToSave = aBlogEntry(new BlogEntryDto(
        new PersistentDto(), blogDto, "smith", "once upon a time"
    ));

    blogEntryRepository.save(blogEntryToSave);
    entityManager.flush();
    entityManager.clear();

    var blogEntries = blogEntryRepository.findAll();

    assertAll(
        () -> assertThat(blogEntries).hasSize(1),
        () -> {
          var blogEntry = blogEntries.iterator().next();

          assertAll(
              () -> assertThat(blogEntry.getCreatorName()).as("entry.creatorName").isEqualTo("smith"),
              () -> assertThat(blogEntry.getTimeOfCreation()).as("entry.creationTime")
                  .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now()),
              () -> assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("once upon a time")
          );
        }
    );
  }

  @Test
  @DisplayName("should write then update and read a blog entry")
  void shouldWriteThenUpdateAndReadBlogEntry() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "dark");
    var blogDto = new BlogDto(new PersistentDto(), LocalDate.now(), "and then some...", userDto);
    var blogEntryToSave = aBlogEntry(new BlogEntryDto(
        new PersistentDto(), blogDto, "smith", "once upon a time"
    ));

    blogEntryRepository.save(blogEntryToSave);
    entityManager.flush();
    entityManager.clear();

    var blogEntries = blogEntryRepository.findAll();

    assertThat(blogEntries).hasSize(1);

    var blogEntry = blogEntries.iterator().next();

    blogEntry.modify("happily ever after", "luke");

    blogEntryRepository.save(blogEntry);
    entityManager.flush();
    entityManager.clear();

    var modifiedEntries = blogEntryRepository.findAll();

    assertThat(modifiedEntries).hasSize(1);

    var modifiedEntry = modifiedEntries.iterator().next();

    assertAll(
        () -> assertThat(modifiedEntry.getCreatorName()).as("entry.creatorName").isEqualTo("luke"),
        () -> assertThat(modifiedEntry.getTimeOfModification()).as("entry.timeOfModification")
            .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now()),
        () -> assertThat(modifiedEntry.getEntry()).as("entry.entry").isEqualTo("happily ever after")
    );
  }
}
