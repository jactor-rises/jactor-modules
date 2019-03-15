package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.guestbook.GuestBookEntity;
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
@DisplayName("A GuestBookRepository")
class GuestBookRepositoryTest {

  @Autowired
  private GuestBookRepository guestBookRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should write then read guest book")
  void shouldWriteThenReadGuestBook() {
    var addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    var userDto = new UserDto(null, personDto, "casuel@tantooine.com", "causual");
    var guestBookEntityToSave = aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", userDto));

    guestBookRepository.save(guestBookEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var guestBookEntity = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

    assertAll(
        () -> assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("home sweet home"),
        () -> assertThat(guestBookEntity.getUser()).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should write then update and read guest book")
  void shouldWriteThenUpdateAndReadGuestBook() {
    var addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    var userDto = new UserDto(null, personDto, "casuel@tantooine.com", "causual");
    var guestBookEntityToSave = aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", userDto));

    guestBookRepository.save(guestBookEntityToSave);
    entityManager.flush();
    entityManager.clear();

    var guestBookEntityToUpdate = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

    guestBookEntityToUpdate.setTitle("5000 thousands miles away from home");

    guestBookRepository.save(guestBookEntityToUpdate);
    entityManager.flush();
    entityManager.clear();

    var guestBookEntity = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

    assertThat(guestBookEntity.getTitle()).isEqualTo("5000 thousands miles away from home");
  }

  private AssertionError guestBookNotFound() {
    return new AssertionError("Guest book not found");
  }
}