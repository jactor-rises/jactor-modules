package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import com.github.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A UserRepository")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @DisplayName("should find default user")
    @Test void shouldFindDefaultUser() {
        Optional<UserEntity> userByName = userRepository.findByUsername("jactor");

        assertAll(
                () -> assertThat(userByName).as("default user").isPresent(),
                () -> {
                    UserEntity userEntity = userByName.orElseThrow(this::userNotFound);
                    assertAll(
                            () -> assertThat(userEntity.getEmailAddress()).as("user email").isEqualTo("tor.egil.jacobsen@gmail.com"),
                            () -> assertThat(userEntity.getPerson().getFirstName()).as("user first name").isEqualTo("Tor Egil")
                    );
                }
        );
    }

    @DisplayName("should write then read a user entity")
    @Test void shouldWriteThenReadUserEntity() {
        UserEntity userToPersist = aUser()
                .with(aPerson())
                .withUsername("smuggler")
                .withEmailAddress("smuggle.fast@tantooine.com")
                .build();

        userRepository.save(userToPersist);
        Optional<UserEntity> userById = userRepository.findById(userToPersist.getId());

        assertAll(
                () -> assertThat(userById).isPresent(),
                () -> {
                    UserEntity userEntity = userById.orElseThrow(this::userNotFound);
                    assertAll(
                            () -> assertThat(userEntity.getPerson()).as("person").isEqualTo(userToPersist.getPerson()),
                            () -> assertThat(userEntity.getUsername()).as("username").isEqualTo("smuggler"),
                            () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("smuggle.fast@tantooine.com")
                    );
                }
        );
    }

    @DisplayName("should write then update and read a user entity")
    @Test void shouldWriteThenUpdateAndReadUserEntity() {
        UserEntity userToPersist = aUser()
                .with(aPerson())
                .withUsername("smuggler")
                .withEmailAddress("smuggle.fast@tantooine.com")
                .build();

        userRepository.save(userToPersist);

        userToPersist.setEmailAddress("luke@force.com");
        userToPersist.setUsername("lukewarm");

        userRepository.save(userToPersist);

        Optional<UserEntity> userByName = userRepository.findByUsername("lukewarm");

        assertAll(
                () -> assertThat(userByName).isPresent(),
                () -> {
                    UserEntity userEntity = userByName.orElseThrow(this::userNotFound);
                    assertAll(
                            () -> assertThat(userEntity.getUsername()).as("username").isEqualTo("lukewarm"),
                            () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("luke@force.com")
                    );
                }
        );
    }

    private AssertionError userNotFound() {
        return new AssertionError("no user found");
    }
}
