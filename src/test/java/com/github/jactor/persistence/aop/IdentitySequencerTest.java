package com.github.jactor.persistence.aop;

import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.PersonEntity.aPerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
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
    var personDto = new PersonDto(null, new AddressDto(), null, null, null, null);
    var first = aPerson(personDto);
    var second = aPerson(personDto);
    var third = aPerson(personDto);

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
  @DisplayName("should set id on a guestBook entity, as well as user and person")
  void shouldSaveGuestBookUserPersonAndAddress() {
    var personDto = new PersonDto(null, new AddressDto(), null, null, null, null);
    var userDto = new UserDto(null, personDto, null, null);
    var guestBook = aGuestBook(new GuestBookDto(null, new HashSet<>(), "title", userDto));
    when(joinPointMock.getArgs()).thenReturn(new Object[]{guestBook});

    identitySequencer.addIdentity(joinPointMock);

    assertAll(
        () -> assertThat(guestBook.getId()).as("guestBook.id").isEqualTo(1000000L),
        () -> assertThat(guestBook.getUser().getId()).as("guestBook.user.id").isEqualTo(1000000L)
    );
  }
}
