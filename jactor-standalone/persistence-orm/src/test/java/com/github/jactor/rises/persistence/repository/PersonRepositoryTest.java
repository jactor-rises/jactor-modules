package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
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
                .with(anAddress().withAddressLine1("somewhere").withZipCode(1234).withCity("Spring"))
                .withDescription("Me, myself, and I")
                .withLocale("no_NO")
                .withFirstName("Turbo")
                .withSurname("Jacobsen")
                .build();

        personRepository.save(personToPersist);
        Optional<PersonEntity> personEntityById = personRepository.findById(personToPersist.getId());

        assertAll(
                () -> assertThat(personEntityById).isPresent(),
                () -> {
                    PersonEntity personEntity = personEntityById.orElseThrow(this::error);
                    assertAll(
                            () -> assertThat(personEntity.getAddressEntity()).as("address").isEqualTo(personEntity.getAddressEntity()),
                            () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("Me, myself, and I"),
                            () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("no_NO"),
                            () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Turbo"),
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Jacobsen")
                    );
                }
        );
    }

    @DisplayName("should write then update a person entity")
    @Test void shouldWriteThenUpdatePersonEntity() {
        PersonEntity personToPersist = aPerson()
                .with(anAddress().withAddressLine1("somewhere").withZipCode(1234).withCity("Spring"))
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

        personRepository.save(personToPersist);

        Optional<PersonEntity> personEntityById = personRepository.findById(personToPersist.getId());

        assertAll(
                () -> assertThat(personEntityById).isPresent(),
                () -> {
                    PersonEntity personEntity = personEntityById.orElseThrow(this::error);
                    assertAll(
                            () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("There is no try"),
                            () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("dk_DK"),
                            () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Dr. A."),
                            () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Cula")
                    );
                }
        );
    }

    private AssertionError error() {
        return new AssertionError("person not found");
    }
}
