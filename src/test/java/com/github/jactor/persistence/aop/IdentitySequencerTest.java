package com.github.jactor.persistence.aop;

import static com.github.jactor.persistence.entity.AddressEntity.anAddress;
import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.PersonEntity.aPerson;
import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.entity.UserEntity;
import java.util.HashSet;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("An IdentitySequencer")
class IdentitySequencerTest {

  private IdentitySequencer identitySequencer = new IdentitySequencer();

  @Mock
  private JoinPoint joinPointMock;

  @BeforeEach
  void setUpMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("should increment the sequence of an entity, and the first number should be 1,000,000")
  void shouldIncrementSequenceOfAddressEntity() {
    var addressDto = new AddressDto(null, 1001, "addressLine1", null, null, "Rud", null);
    var first = anAddress(addressDto);
    var second = anAddress(addressDto);
    var third = anAddress(addressDto);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{first, second, third});

    identitySequencer.addIdentity(joinPointMock);
    identitySequencer.addIdentity(joinPointMock);
    identitySequencer.addIdentity(joinPointMock);

    assertAll(
        () -> assertThat(first.getId()).as("first").isEqualTo(1000000L),
        () -> assertThat(second.getId()).as("second").isEqualTo(1000001L),
        () -> assertThat(third.getId()).as("third").isEqualTo(1000002L)
    );
  }

  @Test
  @DisplayName("should set id on an person entity and address entity as well as the user entity")
  void shouldSetIdOnPersonsAddressAndUser() {
    var personDto = new PersonDto(null, new AddressDto(), null, null, null, null);
    var personEntity = aPerson(personDto);
    personEntity.setUserEntity(new UserEntity(new UserDto()));

    when(joinPointMock.getArgs()).thenReturn(new Object[]{personEntity});

    identitySequencer.addIdentity(joinPointMock);

    assertAll(
        () -> assertThat(personEntity.getId()).as("person.id").isEqualTo(1000000L),
        () -> assertThat(personEntity.getAddressEntity().getId()).as("person.address.id").isEqualTo(1000000L)
    );
  }

  @Test
  @DisplayName("should set id on an user entity as well as person and address")
  void shouldSetIdOnUserPersonAndAddress() {
    var personDto = new PersonDto(null, new AddressDto(), null, null, null, null);
    var user = aUser(new UserDto(null, personDto, null, null));
    when(joinPointMock.getArgs()).thenReturn(new Object[]{user});

    identitySequencer.addIdentity(joinPointMock);

    assertAll(
        () -> assertThat(user.getId()).as("user.id").isEqualTo(1000000L),
        () -> assertThat(user.getPerson().getId()).as("user.person.id").isEqualTo(1000000L),
        () -> assertThat(user.getPerson().getAddressEntity().getId()).as("user.person.address.id").isEqualTo(1000000L)
    );
  }

  @Test
  @DisplayName("should set id on a guestBook entity, as well as user, person, and address")
  void shouldSaveGuestBookUserPersonAndAddress() {
    var personDto = new PersonDto(null, new AddressDto(), null, null, null, null);
    var userDto = new UserDto(null, personDto, null, null);
    var guestBook = aGuestBook(new GuestBookDto(null, new HashSet<>(), "title", userDto));
    when(joinPointMock.getArgs()).thenReturn(new Object[]{guestBook});

    identitySequencer.addIdentity(joinPointMock);

    assertAll(
        () -> assertThat(guestBook.getId()).as("guestBook.id").isEqualTo(1000000L),
        () -> assertThat(guestBook.getUser().getId()).as("guestBook.user.id").isEqualTo(1000000L),
        () -> assertThat(guestBook.getUser().getPerson().getId()).as("guestBook.user.person.id").isEqualTo(1000000L),
        () -> assertThat(guestBook.getUser().getPerson().getAddressEntity().getId()).as("guestBook.user.person.address.id").isEqualTo(1000000L)
    );
  }
}
