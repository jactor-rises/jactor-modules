package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.test.extension.validate.fields.FieldValue;
import com.gitlab.jactor.rises.test.extension.validate.fields.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Map;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("The RepositoriesTest")
class RepositoriesTest {

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            PersonEntity.class, asList(
                    new FieldValue("addressEntity", () -> anAddress().build()),
                    new FieldValue("surname", "sure, man")
            ), AddressEntity.class, asList(
                    new FieldValue("addressLine1", "Test Boulevard 1"),
                    new FieldValue("zipCode", 1001),
                    new FieldValue("city", "Testing")
            )
    ));

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
