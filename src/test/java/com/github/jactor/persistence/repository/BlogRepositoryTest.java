package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.BlogEntity.aBlog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.BlogEntryEntity;
import java.time.LocalDate;
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
@DisplayName("A BlogRepository")
class BlogRepositoryTest {

  @Autowired
  private BlogRepository blogRepositoryToTest;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should save and then read blog entity")
  void shouldSaveThenReadBlogEntity() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");
    var blogEntityToSave = aBlog(new BlogDto(new PersistentDto(), LocalDate.now(), "Blah", userDto));

    blogRepositoryToTest.save(blogEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var blogs = blogRepositoryToTest.findAll();

    assertAll(
        () -> assertThat(blogs).as("blogs").hasSize(1),
        () -> {
          var blogEntity = blogs.iterator().next();
          assertThat(blogEntity.getCreated()).as("created").isEqualTo(LocalDate.now());
          assertThat(blogEntity.getTitle()).as("title").isEqualTo("Blah");
        }
    );
  }

  @Test
  @DisplayName("should save then update and read blog entity")
  void shouldSaveThenUpdateAndReadBlogEntity() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");
    var blogEntityToSave = aBlog(new BlogDto(new PersistentDto(), LocalDate.now(), "Blah", userDto));

    blogRepositoryToTest.save(blogEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var blogs = blogRepositoryToTest.findBlogsByTitle("Blah");

    assertThat(blogs).hasSize(1);

    var blogEntitySaved = blogs.iterator().next();

    blogEntitySaved.setTitle("Duh");

    blogRepositoryToTest.save(blogEntitySaved);
    entityManager.flush();
    entityManager.clear();

    var modifiedBlogs = blogRepositoryToTest.findBlogsByTitle("Duh");

    assertAll(
        () -> assertThat(modifiedBlogs).as("modified blogs").hasSize(1),
        () -> {
          var blogEntity = modifiedBlogs.iterator().next();
          assertThat(blogEntity.getCreated()).as("created").isEqualTo(LocalDate.now());
          assertThat(blogEntity.getTitle()).as("title").isEqualTo("Duh");
        }
    );
  }

  @Test
  @DisplayName("should find blog by title")
  void shouldFindBlogByTitle() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");
    var blogEntityToSave = aBlog(new BlogDto(new PersistentDto(), LocalDate.now(), "Blah", userDto));

    blogRepositoryToTest.save(blogEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var blogs = blogRepositoryToTest.findBlogsByTitle("Blah");

    assertAll(
        () -> assertThat(blogs).as("blogs").hasSize(1),
        () -> assertThat(blogs.get(0)).as("blog").isNotNull(),
        () -> assertThat(blogs.get(0).getCreated()).as("blog.created").isEqualTo(LocalDate.now())
    );
  }

  @Test
  @DisplayName("should be able to relate a blog entry")
  void shouldRelateBlogEntry() {
    var addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    var personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    var userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");
    var blogEntityToSave = aBlog(new BlogDto(new PersistentDto(), LocalDate.now(), "Blah", userDto));

    var blogEntryToSave = new BlogEntryEntity(new BlogEntryDto(
        new PersistentDto(), blogEntityToSave.asDto(), "arnold", "i'll be back"
    ));

    blogEntityToSave.add(blogEntryToSave);

    blogRepositoryToTest.save(blogEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var blogs = blogRepositoryToTest.findBlogsByTitle("Blah");

    assertAll(
        () -> assertThat(blogs).hasSize(1),
        () -> {
          var blogEntity = blogs.iterator().next();
          assertThat(blogEntity.getEntries()).hasSize(1);

          var blogEntryEntity = blogEntity.getEntries().iterator().next();
          assertThat(blogEntryEntity.getEntry()).as("entry").isEqualTo("i'll be back");
          assertThat(blogEntryEntity.getCreatorName()).as("creatorName").isEqualTo("arnold");
        }
    );
  }

  private AssertionError blogNotFound() {
    return new AssertionError("Blog not found");
  }
}