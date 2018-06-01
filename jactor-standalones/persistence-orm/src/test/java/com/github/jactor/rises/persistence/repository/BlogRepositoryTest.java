package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A BlogRepository")
class BlogRepositoryTest {

    @Autowired private BlogRepository blogRepositoryToTest;

    @DisplayName("should save and then read blog entity")
    @Test void shouldSaveThenReadBlogEntity() {
        BlogEntity blogEntityToSave = aBlog()
                .with(aUser())
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);

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

        BlogEntity blogEntitySaved = blogRepositoryToTest.findById(blogEntityToSave.getId()).orElseThrow(this::blogNotFound);
        blogEntitySaved.setTitle("Duh");

        blogRepositoryToTest.save(blogEntitySaved);

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

        List<BlogEntity> blogs = blogRepositoryToTest.findBlogEntitiesByTitle("Blah");

        assertAll(
                () -> assertThat(blogs).as("blogs").hasSize(1),
                () -> assertThat(blogs.get(0)).as("blog").isNotNull(),
                () -> assertThat(blogs.get(0).getCreated()).as("blog.created").isEqualTo(LocalDate.now())
        );
    }

    private AssertionError blogNotFound() {
        return new AssertionError("Blog not found");
    }

    private AssertionError defaultUserNotFound() {
        return new AssertionError("User 'jactor' not found");
    }
}