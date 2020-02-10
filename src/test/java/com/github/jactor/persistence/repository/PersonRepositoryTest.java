package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.PersonEntity.aPerson;
import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.entity.UserEntity;
import java.util.HashSet;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("A PersonRepository")
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
class PersonRepositoryTest {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should find default persons")
  void shouldFindDefaultPersons() {
    var personEntities = personRepository.findBySurname("Jacobsen");
    var firstNames = new HashSet<String>();

    for (PersonEntity personEntity : personEntities) {
      firstNames.add(personEntity.getFirstName());
    }

    assertThat(firstNames).as("first names").contains("Tor Egil", "Suthatip");
  }

  @Test
  @DisplayName("should save then read a person entity")
  void shouldWriteThenReadPersonEntity() {
    int allreadyPresentPeople = numberOf(personRepository.findAll());

    AddressDto address = new AddressDto(null, "1001", "Test Boulevar 1", null, null, "Testington", null);
    PersonEntity personToPersist = aPerson(new PersonInternalDto(
        null, address, "no_NO", "Born", "Sometime", "Me, myself, and I"
    ));

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    var people = personRepository.findAll();

    assertAll(
        () -> assertThat(people).hasSize(allreadyPresentPeople + 1),
        () -> {
          PersonEntity personEntity = personRepository.findBySurname("Sometime").iterator().next();
          assertAll(
              () -> assertThat(personEntity.getAddressEntity()).as("address").isEqualTo(personToPersist.getAddressEntity()),
              () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("Me, myself, and I"),
              () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("no_NO"),
              () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Born")
          );
        }
    );
  }

  @Test
  @DisplayName("should save then update and read a person entity")
  void shouldWriteThenUpdateAndReadPersonEntity() {
    AddressDto addressDto = new AddressDto(null, "1001", "Test Boulevard 1", null, null, "Testington", null);
    PersonEntity personToPersist = aPerson(new PersonInternalDto(
        null,
        addressDto,
        "no_NO",
        "B",
        "Mine",
        "Just me..."
    ));

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    var mine = personRepository.findBySurname("Mine");
    var person = mine.iterator().next();

    person.setDescription("There is no try");
    person.setLocale("dk_DK");
    person.setFirstName("Dr. A.");
    person.setSurname("Cula");

    personRepository.save(person);
    entityManager.flush();
    entityManager.clear();

    var foundCula = personRepository.findBySurname("Cula");
    var personEntity = foundCula.iterator().next();

    assertAll(
        () -> assertThat(personEntity.getDescription()).as("description").isEqualTo("There is no try"),
        () -> assertThat(personEntity.getLocale()).as("locale").isEqualTo("dk_DK"),
        () -> assertThat(personEntity.getFirstName()).as("first name").isEqualTo("Dr. A."),
        () -> assertThat(personEntity.getUsers()).isEqualTo(person.getUsers())
    );
  }

  @Test
  @DisplayName("should be able to relate a user")
  void shouldRelateUser() {
    int allreadyPresentPeople = numberOf(personRepository.findAll());

    AddressDto addressDto = new AddressDto(
        new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testing", null
    );

    PersonInternalDto personInternalDto = new PersonInternalDto(new PersistentDto(), addressDto, null, null, "Adder", null);
    UserInternalDto userInternalDto = new UserInternalDto(new PersistentDto(), personInternalDto, "public@services.com", "black");

    UserEntity userEntity = aUser(userInternalDto);
    PersonEntity personToPersist = userEntity.fetchPerson();

    personRepository.save(personToPersist);
    entityManager.flush();
    entityManager.clear();

    assertAll(
        () -> {
          var people = personRepository.findAll();
          assertThat(people).hasSize(allreadyPresentPeople + 1);
        }, () -> {
          PersonEntity personEntity = personRepository.findBySurname("Adder").iterator().next();
          assertAll(
              () -> assertThat(personEntity.getUsers()).as("user").isNotNull(),
              () -> {
                assertThat(personEntity.getUsers()).as("users").hasSize(1);

                UserEntity persistedUser = personEntity.getUsers().iterator().next();

                assertAll(
                    () -> assertThat(persistedUser.getEmailAddress()).as("user email").isEqualTo("public@services.com"),
                    () -> assertThat(persistedUser.getUsername()).as("user name").isEqualTo("black")
                );
              }
          );
        }
    );
  }

  private int numberOf(Iterable<PersonEntity> people) {
    int counter = 0;
    var peopleIterator = people.iterator();

    for (; peopleIterator.hasNext(); counter++) {
      peopleIterator.next();
    }

    return counter;
  }
}
