package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A UserRepository")
class UserRepositoryTest {

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            UserEntity.class, singletonList(
                    new FieldValue("personEntity", () -> aPerson().build())
            ), PersonEntity.class, asList(
                    new FieldValue("addressEntity", () -> anAddress().build()),
                    new FieldValue("surname", "sure, man")
            ), AddressEntity.class, asList(
                    new FieldValue("addressLine1", "Test Boulevard 1"),
                    new FieldValue("zipCode", 1001),
                    new FieldValue("city", "Testing")
            )
    ));

    private @Autowired UserRepository userRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should find user with username jactor")
    @Test void shouldFindJactor() {
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
        entityManager.flush();
        entityManager.clear();

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
                .withUsername("causual")
                .withEmailAddress("casuel@tantooine.com")
                .build();

        userRepository.save(userToPersist);
        entityManager.flush();
        entityManager.clear();

        String lukewarm = "lukewarm";
        userToPersist.setUsername(lukewarm);
        userToPersist.setEmailAddress("luke@force.com");

        userRepository.save(userToPersist);
        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> userByName = userRepository.findByUsername(lukewarm);

        assertAll(
                () -> assertThat(userByName).isPresent(),
                () -> {
                    UserEntity userEntity = userByName.orElseThrow(this::userNotFound);
                    assertAll(
                            () -> assertThat(userEntity.getUsername()).as("username").isEqualTo(lukewarm),
                            () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("luke@force.com")
                    );
                }
        );
    }

    private AssertionError userNotFound() {
        return new AssertionError("no user found");
    }

    @DisplayName("should find all active users")
    @Test void shouldFindAllActiveUsers() {
        userRepository.save(aUser().withUsername("spiderman").build());
        userRepository.save(aUser().withUsername("ironman").isInactive().build());
        entityManager.flush();
        entityManager.clear();

        List<String> usernames = userRepository.findByInactiveOrderByUsername(false).stream()
                .map(UserRepository.UsernameProjection::getUsername)
                .collect(toList());

        assertThat(usernames).containsExactly("jactor", "spiderman", "tip");
    }
}
