package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A PersonRepository")
class PersonRepositoryTest {

    private @Autowired PersonRepository personRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should find default persons")
    @Test void shouldFindDefaultPersons() {
        List<PersonEntity> personEntities = personRepository.findBySurname("Jacobsen");

        assertAll(
                () -> assertThat(personEntities).hasSize(2),
                () -> {
                    for (PersonEntity personEntity : personEntities) {
                        assertThat(personEntity.getFirstName()).as("first name").isIn("Tor Egil", "Suthatip");
                    }
                }
        );
    }

    @DisplayName("should save then read a person entity")
    @Test void shouldWriteThenReadPersonEntity() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withDescription("Me, myself, and I")
                .withLocale("no_NO")
                .withFirstName("Turbo")
                .withSurname("Jacobsen")
                .build();

        personRepository.save(personToPersist);
        entityManager.flush();
        entityManager.clear();

        Optional<PersonEntity> personById = personRepository.findById(personToPersist.getId());

        assertAll(
                () -> assertThat(personById).isPresent(),
                () -> {
                    PersonEntity personEntity = personById.orElseThrow(this::notFoundError);
                    assertAll(
                            () -> assertThat(personEntity.getAddressEntity()).as("address").isEqualTo(personToPersist.getAddressEntity()),
                            () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("Me, myself, and I"),
                            () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("no_NO"),
                            () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Turbo"),
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Jacobsen")
                    );
                }
        );
    }

    @DisplayName("should save then update and read a person entity")
    @Test void shouldWriteThenUpdateAndReadPersonEntity() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withDescription("Me, myself, and I")
                .withLocale("no_NO")
                .withFirstName("Turbo")
                .withSurname("Jacobsen")
                .build();

        personRepository.save(personToPersist);
        entityManager.flush();
        entityManager.clear();

        PersonEntity personById = personRepository.findById(personToPersist.getId()).orElseThrow(this::notFoundError);

        personById.setDescription("There is no try");
        personById.setLocale("dk_DK");
        personById.setFirstName("Dr. A.");
        personById.setSurname("Cula");
        personById.setUserEntity(
                aUser().with(personById).withUsername("sucker").withEmailAddress("bloody@suckers.com").build()
        );

        personRepository.save(personById);
        entityManager.flush();
        entityManager.clear();

        Optional<PersonEntity> foundPerson = personRepository.findById(personToPersist.getId());

        assertAll(
                () -> assertThat(foundPerson).isPresent(),
                () -> {
                    PersonEntity personEntity = foundPerson.orElseThrow(this::notFoundError);
                    assertAll(
                            () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("There is no try"),
                            () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("dk_DK"),
                            () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Dr. A."),
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Cula"),
                            () -> assertThat(personEntity.getUserEntity()).isEqualTo(personById.getUserEntity())
                    );
                }
        );
    }

    @DisplayName("should be able to relate a user")
    @Test void shouldRelateUser() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withSurname("Adder")
                .with(aUser().withUsername("black").withEmailAddress("public@services.com"))
                .build();

        personRepository.save(personToPersist);
        entityManager.flush();
        entityManager.clear();

        Optional<PersonEntity> personById = personRepository.findById(personToPersist.getId());


        assertAll(
                () -> assertThat(personById).isPresent(),
                () -> {
                    PersonEntity personEntity = personById.orElseThrow(this::notFoundError);
                    assertAll(
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Adder"),
                            () -> assertThat(personEntity.getUserEntity()).as("user").isNotNull(),
                            () -> assertThat(personEntity.getUserEntity().getEmailAddress()).as("user email").isEqualTo("public@services.com"),
                            () -> assertThat(personEntity.getUserEntity().getUsername()).as("user name").isEqualTo("black")
                    );
                }
        );
    }

    private AssertionError notFoundError() {
        return new AssertionError("person not found");
    }
}
