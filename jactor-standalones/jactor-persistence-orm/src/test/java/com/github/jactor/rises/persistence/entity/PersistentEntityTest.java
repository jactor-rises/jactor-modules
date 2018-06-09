package com.github.jactor.rises.persistence.entity;

import com.github.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactor.rises.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersistentEntity")
@ExtendWith(RequiredFieldsExtension.class)
class PersistentEntityTest {

    private PersistentEntity<?> persistentEntityToTest;

    @DisplayName("should be able to copy an address without the id")
    @Test void shouldCopyAddress() {
        persistentEntityToTest = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("out")
                .withAddressLine3("there")
                .withCity("svg")
                .withCountryCode("NO")
                .withZipCode(1001)
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a person without the id")
    @Test void shouldCopyPerson() {
        persistentEntityToTest = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("here i am")
                .withFirstName("Bill")
                .withSurname("Smith")
                .withLocale("us_US")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a user without the id")
    @Test void shouldCopyUser() {
        persistentEntityToTest = aUser()
                .with(aPerson())
                .withEmailAddress("i.am@home")
                .withUserName("jactor")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a blog without the id")
    @Test void shouldCopyBlog() {
        persistentEntityToTest = aBlog()
                .with(aUser())
                .withTitle("general ignorance")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a blog entry without the id")
    @Test void shouldCopyBlogEntry() {
        persistentEntityToTest = aBlogEntry()
                .with(aBlog())
                .withCreatorName("jactor")
                .withEntry("the one")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a guest book without the id")
    @Test void shouldCopyGuestBook() {
        persistentEntityToTest = aGuestBook()
                .with(aUser())
                .withTitle("enter when applied")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }

    @DisplayName("should be able to copy a guest book entry without the id")
    @Test void shouldCopyGuestBookEntry() {
        persistentEntityToTest = aGuestBookEntry()
                .with(aGuestBook())
                .withCreatorName("jactor")
                .withEntry("the one")
                .build().addSequencedId(aClass -> 1L);

        PersistentEntity<?> copy = persistentEntityToTest.copy();

        assertAll(
                () -> assertThat(persistentEntityToTest).as("persistent entity").isNotNull(),
                () -> assertThat(persistentEntityToTest.getId()).as("id of persistent entity").isEqualTo(1L),
                () -> assertThat(copy).as("copy").isNotNull(),
                () -> assertThat(copy.getId()).as("id of copy").isNull(),
                () -> assertThat(persistentEntityToTest).as("persistent entity equals copy").isEqualTo(copy),
                () -> assertThat(persistentEntityToTest).as("persistent entity is not same instance as copy").isNotSameAs(copy)
        );
    }
}
