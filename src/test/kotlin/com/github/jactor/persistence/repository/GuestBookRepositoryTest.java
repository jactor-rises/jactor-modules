package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressInternalDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.dto.UserInternalDto;
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
  private UserRepository userRepository;
  @Autowired
  private GuestBookRepository guestBookRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should write then read guest book")
  void shouldWriteThenReadGuestBook() {
    var addressDto = new AddressInternalDto(new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonInternalDto(new PersistentDto(), addressDto, null, null, "AA", null);
    var userDto = new UserInternalDto(new PersistentDto(), personDto, "casuel@tantooine.com", "causual");
    var userEntity = userRepository.save(aUser(userDto));

    userEntity.setGuestBook(
        aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", userEntity.asDto()))
    );

    entityManager.flush();
    entityManager.clear();

    var guestBookEntity = guestBookRepository.findByUser(userEntity);

    assertAll(
        () -> assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("home sweet home"),
        () -> assertThat(guestBookEntity.getUser()).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should write then update and read guest book")
  void shouldWriteThenUpdateAndReadGuestBook() {
    var addressDto = new AddressInternalDto(new PersistentDto(), "1001", "Test Boulevard 1", null, null, "Testington", null);
    var personDto = new PersonInternalDto(new PersistentDto(), addressDto, null, null, "AA", null);
    var userDto = new UserInternalDto(new PersistentDto(), personDto, "casuel@tantooine.com", "causual");
    var userEntity = userRepository.save(aUser(userDto));

    userEntity.setGuestBook(
        aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "home sweet home", userEntity.asDto()))
    );

    guestBookRepository.save(userEntity.getGuestBook());
    entityManager.flush();
    entityManager.clear();

    var guestBookEntityToUpdate = guestBookRepository.findByUser(userEntity);

    guestBookEntityToUpdate.setTitle("5000 thousands miles away from home");

    guestBookRepository.save(guestBookEntityToUpdate);
    entityManager.flush();
    entityManager.clear();

    var guestBookEntity = guestBookRepository.findByUser(userEntity);

    assertThat(guestBookEntity.getTitle()).isEqualTo("5000 thousands miles away from home");
  }
}