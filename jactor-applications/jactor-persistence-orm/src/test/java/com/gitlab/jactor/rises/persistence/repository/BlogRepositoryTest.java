package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A BlogRepository")
class BlogRepositoryTest {

   private  @Autowired BlogRepository blogRepositoryToTest;
   private @Autowired EntityManager entityManager;

    @DisplayName("should save and then read blog entity")
    @Test void shouldSaveThenReadBlogEntity() {
        BlogEntity blogEntityToSave = aBlog()
                .with(aUser())
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        Optional<BlogEntity> blogById = blogRepositoryToTest.findById(blogEntityToSave.getId());

        assertAll(
                () -> assertThat(blogById).as("blog").isPresent(),
                () -> {
                    BlogEntity blogEntity = blogById.orElseThrow(this::blogNotFound);
                    assertThat(blogEntity.getCreated()).as("created").isEqualTo(LocalDate.now());
                    assertThat(blogEntity.getTitle()).as("title").isEqualTo("Blah");
                }
        );
    }

    @DisplayName("should save then update and read blog entity")
    @Test void shouldSaveThenUpdateAndReadBlogEntity() {
        BlogEntity blogEntityToSave = aBlog()
                .with(aUser())
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntity blogEntitySaved = blogRepositoryToTest.findById(blogEntityToSave.getId()).orElseThrow(this::blogNotFound);
        blogEntitySaved.setTitle("Duh");

        blogRepositoryToTest.save(blogEntitySaved);
        entityManager.flush();
        entityManager.clear();

        Optional<BlogEntity> blogById = blogRepositoryToTest.findById(blogEntityToSave.getId());

        assertAll(
                () -> assertThat(blogById).as("blog").isPresent(),
                () -> {
                    BlogEntity blogEntity = blogById.orElseThrow(this::blogNotFound);
                    assertThat(blogEntity.getCreated()).as("created").isEqualTo(LocalDate.now());
                    assertThat(blogEntity.getTitle()).as("title").isEqualTo("Duh");
                }
        );
    }

    @DisplayName("should find blog by title")
    @Test void shouldFindBlogByTitle() {
        BlogEntity blogEntityToSave = aBlog()
                .with(aUser())
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        List<BlogEntity> blogs = blogRepositoryToTest.findBlogsByTitle("Blah");

        assertAll(
                () -> assertThat(blogs).as("blogs").hasSize(1),
                () -> assertThat(blogs.get(0)).as("blog").isNotNull(),
                () -> assertThat(blogs.get(0).getCreated()).as("blog.created").isEqualTo(LocalDate.now())
        );
    }

    @DisplayName("should be able to relate a blog entry")
    @Test void shouldRelateBlogEntry() {
        BlogEntity blogEntityToSave = aBlog()
                .with(aUser())
                .withTitle("Blah")
                .build();

        BlogEntryEntity blogEntryToSave = aBlogEntry().withCreatorName("arnold").withEntry("i'll be back").with(blogEntityToSave).build();
        blogEntityToSave.add(blogEntryToSave);

        blogRepositoryToTest.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntity blogById = blogRepositoryToTest.findById(blogEntityToSave.getId()).orElseThrow(this::blogNotFound);

        assertAll(
                () -> assertThat(blogById.getEntries()).as("entries").hasSize(1),
                () -> {
                    BlogEntryEntity blogEntryEntity = blogById.getEntries().iterator().next();
                    assertThat(blogEntryEntity.getEntry()).as("entry").isEqualTo("i'll be back");
                    assertThat(blogEntryEntity.getCreatorName()).as("creatorName").isEqualTo("arnold");
                }
        );
    }

    private AssertionError blogNotFound() {
        return new AssertionError("Blog not found");
    }
}