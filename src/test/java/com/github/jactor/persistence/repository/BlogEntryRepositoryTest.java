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
import com.github.jactor.persistence.entity.BlogEntity;
import com.github.jactor.persistence.entity.BlogEntryEntity;
import com.github.jactor.persistence.entity.UserEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
  private BlogRepository blogRepository;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("should save then read blog entry")
  void shouldSaveThenReadBlogEntry() {
    var addressDto = new AddressDto(
        new PersistentDto(), 1001, "Test Boulevard 1", null, null, "Testing", null
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

    var blogEntryById = blogEntryRepository.findById(blogEntryToSave.getId()).orElseThrow(this::entryNotFound);

    assertAll(
        () -> assertThat(blogEntryById.getCreatorName()).as("entry.creatorName").isEqualTo("smith"),
        () -> assertThat(blogEntryById.getCreationTime()).as("entry.creationTime")
            .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now()),
        () -> assertThat(blogEntryById.getEntry()).as("entry.entry").isEqualTo("once upon a time")
    );
  }

  @Test
  @DisplayName("should write then update and read a blog entry")
  void shouldWriteThenUpdateAndReadBlogEntry() {
    var addressDto = new AddressDto(
        new PersistentDto(), 1001, "Test Boulevard 1", null, null, "Testing", null
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

    var blogEntryById = blogEntryRepository.findById(blogEntryToSave.getId()).orElseThrow(this::entryNotFound);

    blogEntryById.setCreatorName("luke");
    blogEntryById.update("happily ever after");

    blogEntryRepository.save(blogEntryById);

    var possibleUpdatedEntry = blogEntryRepository.findById(blogEntryToSave.getId());
    entityManager.flush();
    entityManager.clear();

    assertAll(
        () -> assertThat(possibleUpdatedEntry).as("possibleUpdatedEntry").isPresent(),
        () -> {
          BlogEntryEntity blogEntryEntity = possibleUpdatedEntry.orElseThrow(this::entryNotFound);

          assertAll(
              () -> assertThat(blogEntryEntity.getCreatorName()).as("entry.creatorName").isEqualTo("luke"),
              () -> assertThat(blogEntryEntity.getUpdatedTime()).as("entry.updatedTime")
                  .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now()),
              () -> assertThat(blogEntryEntity.getEntry()).as("entry.entry").isEqualTo("happily ever after")
          );
        }
    );
  }

  @Test
  @DisplayName("should write two entries and on two blogs then find entry for the right blog")
  void shouldWriteTwoEntriesOnTwoBlogsThenFindEntryOnBlog() {
    var addressDto = new AddressDto(
        new PersistentDto(), 1001, "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");
    var savedUser = userRepository.save(new UserEntity(userDto));
    var blogDto = new BlogDto(new PersistentDto(), LocalDate.now(), "see you later alligator", savedUser.asDto());

    blogEntryRepository.save(aBlogEntry(new BlogEntryDto(
        new PersistentDto(), blogDto, "someone", "jadda"
    )));

    var knownBlog = new BlogEntity(new BlogDto(new PersistentDto(), LocalDate.now(), "out there", savedUser.asDto()));

    blogRepository.save(knownBlog);

    var blogEntryToSave = aBlogEntry(new BlogEntryDto(
        new PersistentDto(), knownBlog.asDto(), "shrek", "far far away"
    ));

    blogEntryRepository.save(blogEntryToSave);
    entityManager.flush();
    entityManager.clear();

    List<BlogEntryEntity> entriesByBlog = blogEntryRepository.findByBlog_Id(knownBlog.getId());

    assertAll(
        () -> assertThat(blogEntryRepository.findAll()).as("all entries").hasSize(2),
        () -> assertThat(entriesByBlog).as("entriesByBlog").hasSize(1),
        () -> assertThat(entriesByBlog.get(0).getCreatorName()).as("entry.creatorName").isEqualTo("shrek"),
        () -> assertThat(entriesByBlog.get(0).getEntry()).as("entry.entry").isEqualTo("far far away")
    );
  }

  private AssertionError entryNotFound() {
    return new AssertionError("Blog Entry not found");
  }
}
