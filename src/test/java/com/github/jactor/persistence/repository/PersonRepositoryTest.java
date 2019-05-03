package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.PersonEntity.aPerson;
import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A PersonRepository")
class PersonRepositoryTest {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should find default persons")
  void shouldFindDefaultPersons() {
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

  @Test
  @DisplayName("should save then read a person entity")
  void shouldWriteThenReadPersonEntity() {
    AddressDto address = new AddressDto(null, "1001", "Test Boulevar 1", null, null, "Testington", null);
    PersonEntity personToPersist = aPerson(new PersonDto(
        null, address, "no_NO", "Turbo", "Jacobsen", "Me, myself, and I"
    ));

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    var people = personRepository.findAll();

    assertAll(
        () -> assertThat(people).hasSize(3), // two users already present...
        () -> {
          PersonEntity personEntity = people.iterator().next();
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

  @Test
  @DisplayName("should save then update and read a person entity")
  void shouldWriteThenUpdateAndReadPersonEntity() {
    AddressDto addressDto = new AddressDto(null, "1001", "Test Boulevard 1", null, null, "Testington", null);
    PersonEntity personToPersist = aPerson(new PersonDto(
        null,
        addressDto,
        "no_NO",
        "Dr. A",
        "Culn",
        "There is no try"
    ));

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    var people = personRepository.findAll();

    assertThat(people).hasSize(3); // two users already present...
    var person = people.iterator().next();

    person.setDescription("There is no try");
    person.setLocale("dk_DK");
    person.setFirstName("Dr. A.");
    person.setSurname("Cula");

    personRepository.save(person);
    entityManager.flush();
    entityManager.clear();

    var foundPeople = personRepository.findAll();

    assertAll(
        () -> assertThat(foundPeople).hasSize(3), // two users already present...
        () -> {
          PersonEntity personEntity = foundPeople.iterator().next();

          assertAll(
              () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("There is no try"),
              () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("dk_DK"),
              () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Dr. A."),
              () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Cula"),
              () -> assertThat(personEntity.getUserEntity()).isEqualTo(person.getUserEntity())
          );
        }
    );
  }

  @Test
  @DisplayName("should be able to relate a user")
  void shouldRelateUser() {
    AddressDto addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    PersonDto personDto = new PersonDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    UserDto userDto = new UserDto(new PersistentDto(), personDto, "public@services.com", "black");

    UserEntity userEntity = aUser(userDto);
    PersonEntity personToPersist = userEntity.fetchPerson();

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    var people = personRepository.findAll();

    assertAll(
        () -> assertThat(people).hasSize(3), // two users already present...
        () -> {
          PersonEntity personEntity = people.iterator().next();
          assertAll(
              () -> assertThat(personEntity.getSurname()).as("surname").isEqualTo("Adder"),
              () -> assertThat(personEntity.getUserEntity()).as("user").isNotNull(),
              () -> assertThat(personEntity.getUserEntity().getEmailAddress()).as("user email").isEqualTo("public@services.com"),
              () -> assertThat(personEntity.getUserEntity().getUsername()).as("user name").isEqualTo("black")
          );
        }
    );
  }
}
