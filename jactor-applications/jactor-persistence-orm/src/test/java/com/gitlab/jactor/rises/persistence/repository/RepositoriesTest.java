package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("The RepositoriesTest")
class RepositoriesTest {

    private @Autowired BlogRepository blogRepository;
    private @Autowired UserRepository userRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should use a BlogRepository to save a blog with a user that was saved with a UserRepository earlier")
    @Test void shouldSaveBlogWithSavedUser() {
        UserEntity userToPersist = aUser()
                .with(aPerson())
                .withUsername("r2d2")
                .withEmailAddress("brains@rebels.com")
                .build();

        userRepository.save(userToPersist);
        entityManager.flush();
        entityManager.clear();

        UserEntity userById = userRepository.findById(userToPersist.getId()).orElseThrow(() -> new AssertionError("User not found!"));

        BlogEntity blogEntityToSave = aBlog()
                .with(userById)
                .withTitle("Far, far away...")
                .build();

        blogRepository.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntity blogById = blogRepository.findById(blogEntityToSave.getId()).orElseThrow(() -> new AssertionError("Blog not found"));

        assertAll(
                () -> assertThat(blogById.getTitle()).as("blog.title").isEqualTo("Far, far away..."),
                () -> assertThat(blogById.getUser()).as("blog.user").isEqualTo(userById)
        );
    }

    @DisplayName("should not need to \"reattach\" entities already saved")
    @Test void shouldNot11NeedToReattachEntities() {
        UserEntity userToPersist = aUser()
                .with(aPerson())
                .withUsername("c3po")
                .withEmailAddress("language@rebels.com")
                .build();

        userRepository.save(userToPersist);
        entityManager.flush();
        entityManager.clear();

        UserEntity userById = userRepository.findById(userToPersist.getId()).orElseThrow(() -> new AssertionError("User not found!"));

        BlogEntity blogEntityToSave = aBlog()
                .with(new UserEntity(userById.asDto()))
                .withTitle("Say you, say me")
                .build();

        blogRepository.save(blogEntityToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntity blogById = blogRepository.findById(blogEntityToSave.getId()).orElseThrow(() -> new AssertionError("Blog not found"));

        assertAll(
                () -> assertThat(blogById.getTitle()).as("blog.title").isEqualTo("Say you, say me"),
                () -> assertThat(blogById.getUser()).as("blog.user").isEqualTo(userById)
        );
    }
}
