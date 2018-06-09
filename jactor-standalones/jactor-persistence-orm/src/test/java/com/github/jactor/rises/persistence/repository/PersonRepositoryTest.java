package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A PersonRepository")
class PersonRepositoryTest {

    @Autowired private PersonRepository personRepository;

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

    @DisplayName("should write then read a person entity")
    @Test void shouldWriteThenReadPersonEntity() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withDescription("Me, myself, and I")
                .withLocale("no_NO")
                .withFirstName("Turbo")
                .withSurname("Jacobsen")
                .build();

        personRepository.save(personToPersist);
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

    @DisplayName("should write then update and read a person entity")
    @Test void shouldWriteThenUpdateAndReadPersonEntity() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withDescription("Me, myself, and I")
                .withLocale("no_NO")
                .withFirstName("Turbo")
                .withSurname("Jacobsen")
                .build();

        personRepository.save(personToPersist);

        personToPersist.setDescription("There is no try");
        personToPersist.setLocale("dk_DK");
        personToPersist.setFirstName("Dr. A.");
        personToPersist.setSurname("Cula");
        personToPersist.setUserEntity(
                aUser().with(personToPersist).withUserName("sucker").withEmailAddress("bloody@suckers.com").build()
        );

        personRepository.save(personToPersist);

        Optional<PersonEntity> personById = personRepository.findById(personToPersist.getId());

        assertAll(
                () -> assertThat(personById).isPresent(),
                () -> {
                    PersonEntity personEntity = personById.orElseThrow(this::notFoundError);
                    assertAll(
                            () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("There is no try"),
                            () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("dk_DK"),
                            () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Dr. A."),
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Cula"),
                            () -> assertThat(personEntity.getUserEntity()).isEqualTo(personToPersist.getUserEntity())
                    );
                }
        );
    }

    @DisplayName("should be able to relate a user")
    @Test void shouldRelateUser() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress())
                .withSurname("Adder")
                .with(aUser().withUserName("black").withEmailAddress("public@services.com"))
                .build();

        personRepository.save(personToPersist);

        Optional<PersonEntity> personById = personRepository.findById(personToPersist.getId());


        assertAll(
                () -> assertThat(personById).isPresent(),
                () -> {
                    PersonEntity personEntity = personById.orElseThrow(this::notFoundError);
                    assertAll(
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Adder"),
                            () -> assertThat(personEntity.getUserEntity()).as("user").isNotNull(),
                            () -> assertThat(personEntity.getUserEntity().getEmailAddress()).as("user email").isEqualTo("public@services.com"),
                            () -> assertThat(personEntity.getUserEntity().getUserName()).as("user name").isEqualTo("black")
                    );
                }
        );
    }

    private AssertionError notFoundError() {
        return new AssertionError("person not found");
    }
}
