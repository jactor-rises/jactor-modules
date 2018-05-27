package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A BlogRepository")
class BlogRepositoryTest {

    @Autowired private BlogRepository blogRepositoryToTest;
    @Autowired private UserRepository userRepository;

    @DisplayName("should save and then read blog entity")
    @Test void shouldSaveThenReadBlogEntity() {
        BlogEntity blogEntityToSave = aBlog()
                .with(userRepository.findByUserName("jactor").orElseThrow(this::defaultUserNotFound))
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);

        Optional<BlogEntity> blogById = blogRepositoryToTest.findById(blogEntityToSave.getId());

        assertAll(
                () -> assertThat(blogById).as("blog").isPresent(),
                () -> assertThat(
                        blogById.orElseThrow(this::blogNotFound).getTitle()
                ).as("title").isEqualTo("Blah")
        );
    }

    @DisplayName("should save then update and read blog entity")
    @Test void shouldSaveThenUpdateAndReadBlogEntity() {
        BlogEntity blogEntityToSave = aBlog()
                .with(userRepository.findByUserName("jactor").orElseThrow(this::defaultUserNotFound))
                .withTitle("Blah")
                .build();

        blogRepositoryToTest.save(blogEntityToSave);

        BlogEntity blogEntitySaved = blogRepositoryToTest.findById(blogEntityToSave.getId()).orElseThrow(this::blogNotFound);
        blogEntitySaved.setTitle("Duh");

        blogRepositoryToTest.save(blogEntitySaved);

        Optional<BlogEntity> blogById = blogRepositoryToTest.findById(blogEntityToSave.getId());

        assertAll(
                () -> assertThat(blogById).as("blog").isPresent(),
                () -> assertThat(
                        blogById.orElseThrow(this::blogNotFound).getTitle()
                ).as("title").isEqualTo("Duh")
        );
    }

    private AssertionError blogNotFound() {
        return new AssertionError("Blog not found");
    }

    private AssertionError defaultUserNotFound() {
        return new AssertionError("User 'jactor' not found");
    }
}