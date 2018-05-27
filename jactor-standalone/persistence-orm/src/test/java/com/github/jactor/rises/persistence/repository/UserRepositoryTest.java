package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A UserRepository")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private PersonRepository personRepository;

    @DisplayName("should find default user")
    @Test void shouldFindDefaultUser() {
        Optional<UserEntity> userByName = userRepository.findByUserName("jactor");

        assertAll(
                () -> assertThat(userByName).as("default user").isPresent(),
                () -> {
                    UserEntity userEntity = userByName.orElseThrow(this::error);
                    assertThat(userEntity.getEmailAddress()).as("user email").isEqualTo("tor.egil.jacobsen@gmail.com");
                    assertThat(userEntity.getPerson().getFirstName()).as("user first name").isEqualTo("Tor Egil");
                }
        );
    }

    @DisplayName("should write then read a user entity")
    @Test void shouldWriteThenReadUserEntity() {
        UserEntity userToPersist = aUser()
                .with(aPerson()
                        .withSurname("Solo")
                        .with(anAddress()
                                .withAddressLine1("Far far away")
                                .withZipCode(1001)
                                .withCity("Tantooine")
                        )
                ).withUserName("smuggler")
                .withEmailAddress("smuggle.fast@tantooine.com")
                .build();

        userRepository.save(userToPersist);
        Optional<UserEntity> userEntityById = userRepository.findById(userToPersist.getId());

        assertAll(
                () -> assertThat(userEntityById).isPresent(),
                () -> {
                    UserEntity userEntity = userEntityById.orElseThrow(this::error);
                    assertAll(
                            () -> assertThat(userEntity.getPerson()).as("person").isEqualTo(userToPersist.getPerson()),
                            () -> assertThat(userEntity.getUserName()).as("userName").isEqualTo("smuggler"),
                            () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("smuggle.fast@tantooine.com")
                    );
                }
        );
    }

    @DisplayName("should write then update a user entity")
    @Test void shouldWriteThenUpdateUserEntity() {
        UserEntity userToPersist = aUser()
                .with(aPerson()
                        .withSurname("Solo")
                        .with(anAddress()
                                .withAddressLine1("Far far away")
                                .withZipCode(1001)
                                .withCity("Tantooine")
                        )
                ).withUserName("smuggler")
                .withEmailAddress("smuggle.fast@tantooine.com")
                .build();

        userRepository.save(userToPersist);

        userToPersist.setEmailAddress("luke@force.com");
        userToPersist.setUserName("lukewarm");

        userRepository.save(userToPersist);

        Optional<UserEntity> userByName = userRepository.findByUserName("lukewarm");

        assertAll(
                () -> assertThat(userByName).isPresent(),
                () -> {
                    UserEntity userEntity = userByName.orElseThrow(this::error);
                    assertAll(
                            () -> assertThat(userEntity.getUserName()).as("userName").isEqualTo("lukewarm"),
                            () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("luke@force.com")
                    );
                }
        );
    }

    private AssertionError error() {
        return new AssertionError("no user found");
    }
}
