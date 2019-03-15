package com.github.jactor.persistence.entity;

import static com.github.jactor.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactor.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static com.github.jactor.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static com.github.jactor.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.persistence.entity.user.UserEntity.aUser;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A PersistentEntity")
class PersistentEntityTest {

  private PersistentEntity persistentEntityToTest;

  @Test
  @DisplayName("should be able to copy an address without the id")
  void shouldCopyAddress() {
    persistentEntityToTest = anAddress(
        new AddressDto(null, 1001, "somewhere", "out", "there", "svg", "NO")
    ).addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a person without the id")
  void shouldCopyPerson() {
    persistentEntityToTest = aPerson(
        new PersonDto(null, new AddressDto(), "us_US", "Bill", "Smith", "here i am")
    ).addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a user without the id")
  void shouldCopyUser() {
    persistentEntityToTest = aUser(new UserDto(null, null, "i.am@home", "jactor"))
        .addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a blog without the id")
  void shouldCopyBlog() {
    persistentEntityToTest = aBlog(new BlogDto(null, null, "general ignorance", new UserDto()))
        .addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a blog entry without the id")
  void shouldCopyBlogEntry() {
    BlogEntryDto blogEntryDto = new BlogEntryDto(null, new BlogDto(), "jactor", "the one");
    persistentEntityToTest = aBlogEntry(blogEntryDto).addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a guest book without the id")
  void shouldCopyGuestBook() {
    persistentEntityToTest = aGuestBook(new GuestBookDto(null, new HashSet<>(), "enter when applied", new UserDto()))
        .addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should be able to copy a guest book entry without the id")
  void shouldCopyGuestBookEntry() {
    persistentEntityToTest = aGuestBookEntry(
        new GuestBookEntryDto(null, new GuestBookDto(), "jactor", "the one")
    ).addSequencedId(aClass -> 1L);

    PersistentEntity copy = persistentEntityToTest.copy();

    assertAll(
        () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
        () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
        () -> assertThat(copy).as("copy").isNotNull(),
        () -> assertThat(copy.getId()).as("id of copy").isNull(),
        () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
        () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
    );
  }

  @Test
  @DisplayName("should return an empty stream when no dependencies")
  void shouldReturnEmptyStreamWithoutDependenciesGiven() {
    persistentEntityToTest = anAddress(new AddressDto());
    Stream none = persistentEntityToTest.streamSequencedDependencies();

    assertThat(none).isEmpty();
  }

  @Test
  @DisplayName("should stream optional dependencies of an persistent entity")
  void shouldStreamOptionalDependencies() {
    var blogEntity = aBlog(new BlogDto());
    var personEntity = aPerson(new PersonDto());

    persistentEntityToTest = personEntity;
    List<PersistentEntity> dependencies = persistentEntityToTest.streamSequencedDependencies(blogEntity, personEntity, null, null)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());

    assertThat(dependencies)
        .hasSize(2)
        .contains(blogEntity, personEntity);
  }

  @Test
  @DisplayName("should fetch all dependencies of a persistent entity")
  void shouldFetchAllDependencies() {
    var addressDto = new AddressDto();
    var personDto = new PersonDto(null, addressDto, null, null, null, null);
    persistentEntityToTest = aUser(new UserDto(null, personDto, null, null));

    var allSequencedDependencies = persistentEntityToTest.fetchAllSequencedDependencies();

    assertThat(allSequencedDependencies)
        .hasSize(2)
        .contains(anAddress(addressDto), aPerson(personDto));
  }

  @Test
  @DisplayName("should add sequenced id, also on dependencies of a persistent entity")
  void shouldAddSequencedIdAlsOnDependencies() {
    var addressDto = new AddressDto();
    var personDto = new PersonDto(null, addressDto, null, null, null, null);
    var userEntity = aUser(new UserDto(null, personDto, null, null));
    var personEntity = userEntity.getPerson();
    var addressEntity = personEntity.getAddressEntity();
    persistentEntityToTest = userEntity;

    PersistentEntity.Sequencer sequencerMock = mock(PersistentEntity.Sequencer.class);
    when(sequencerMock.nextVal(any(Class.class))).thenReturn(123L);

    persistentEntityToTest.addSequencedId(sequencerMock);

    assertAll(
        () -> assertThat(persistentEntityToTest.getId()).as("persistentEntityToTest.id").isEqualTo(123L),
        () -> assertThat(addressEntity.getId()).as("addressEntity.id").isEqualTo(123L),
        () -> assertThat(personEntity.getId()).as("personEntity.id").isEqualTo(123L)
    );
  }
}
