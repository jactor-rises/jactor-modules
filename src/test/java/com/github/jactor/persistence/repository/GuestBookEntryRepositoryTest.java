package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.GuestBookEntryEntity.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.GuestBookEntryEntity;
import com.github.jactor.persistence.entity.UserEntity;
import java.util.HashSet;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A GuestBookEntryRepository")
class GuestBookEntryRepositoryTest {

  @Autowired
  private GuestBookEntryRepository guestBookEntryRepository;
  @Autowired
  private GuestBookRepository guestBookRepository;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("should save then read guest book entry entity")
  void shouldSaveThenReadGuestBookEntryEntity() {
    var addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    var userDto = new UserDto(null, personDto, "casuel@tantooine.com", "causual");

    var savedUser = userRepository.save(new UserEntity(userDto));
    var guestBook = aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", savedUser.asDto()));

    guestBookRepository.save(guestBook);

    var guestBookEntryEntityToSave = aGuestBookEntry(
        new GuestBookEntryDto(new PersistentDto(), guestBook.asDto(), "Harry", "Draco Dormiens Nunquam Tittilandus")
    );

    guestBookEntryRepository.save(guestBookEntryEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var entriesByGuestBook = guestBookEntryRepository.findByGuestBook(guestBook);

    assertAll(
        () -> assertThat(entriesByGuestBook).hasSize(1),
        () -> assertThat(entriesByGuestBook.iterator().next()).extracting(GuestBookEntryEntity::getCreatorName).as("creator name").isEqualTo("Harry"),
        () -> assertThat(entriesByGuestBook.iterator().next()).extracting(GuestBookEntryEntity::getEntry).as("entry")
            .isEqualTo("Draco Dormiens Nunquam Tittilandus")
    );
  }

  @Test
  @DisplayName("should save then update and read guest book entry entity")
  void shouldSaveThenUpdateAndReadGuestBookEntryEntity() {
    var addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    var userDto = new UserDto(null, personDto, "casuel@tantooine.com", "causual");

    var savedUser = userRepository.save(new UserEntity(userDto));
    var guestBook = aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", savedUser.asDto()));

    guestBookRepository.save(guestBook);

    var guestBookEntryEntityToSave = aGuestBookEntry(
        new GuestBookEntryDto(new PersistentDto(), guestBook.asDto(), "Harry", "Draco Dormiens Nunquam Tittilandus")
    );

    guestBookEntryRepository.save(guestBookEntryEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var entriesByGuestBook = guestBookEntryRepository.findByGuestBook(guestBook);

    assertThat(entriesByGuestBook).hasSize(1);

    entriesByGuestBook.iterator().next().modify("Willie", "On the road again");

    guestBookEntryRepository.save(entriesByGuestBook.iterator().next());
    entityManager.flush();
    entityManager.clear();

    var modifiedEntryByGuestBook = guestBookEntryRepository.findByGuestBook(guestBook);

    assertAll(
        () -> assertThat(modifiedEntryByGuestBook).as("entries").hasSize(1),
        () -> assertThat(modifiedEntryByGuestBook.iterator().next()).extracting(GuestBookEntryEntity::getCreatorName).as("creator name")
            .isEqualTo("Willie"),
        () -> assertThat(modifiedEntryByGuestBook.iterator().next()).extracting(GuestBookEntryEntity::getEntry).as("entry")
            .isEqualTo("On the road again")
    );
  }

  @Test
  @DisplayName("should write two entries to two different guest books and then find one entry")
  void shouldWriteTwoEntriesToTwoGuestBooksAndThenFindEntry() {
    var addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    var userDto = new UserDto(null, personDto, "casuel@tantooine.com", "causual");

    var savedUser = userRepository.save(new UserEntity(userDto));
    var guestbookDto = new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", savedUser.asDto());

    guestBookEntryRepository.save(aGuestBookEntry(
        new GuestBookEntryDto(new PersistentDto(), guestbookDto, "somone", "jadda"))
    );

    var guestBookToSave = aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", savedUser.asDto()));

    guestBookRepository.save(guestBookToSave);
    entityManager.flush();
    entityManager.clear();

    var guestBookEntryToSave = aGuestBookEntry(
        new GuestBookEntryDto(new PersistentDto(), guestBookToSave.asDto(), "shrek", "far far away")
    );

    guestBookEntryRepository.save(guestBookEntryToSave);
    entityManager.flush();
    entityManager.clear();

    var entriesByGuestBook = guestBookEntryRepository.findByGuestBook(guestBookToSave);

    assertAll(
        () -> assertThat(guestBookEntryRepository.findAll()).as("all entries").hasSize(2),
        () -> assertThat(entriesByGuestBook).as("entriesByGuestBook").hasSize(1),
        () -> assertThat(entriesByGuestBook.get(0).getCreatorName()).as("entry.creatorName").isEqualTo("shrek"),
        () -> assertThat(entriesByGuestBook.get(0).getEntry()).as("entry.entry").isEqualTo("far far away")
    );
  }

  private AssertionError entryNotFound(GuestBookEntryEntity guestBookEntryEntity) {
    return new AssertionError(String.format("guest book entry with id=%s not found", guestBookEntryEntity.getId()));
  }
}