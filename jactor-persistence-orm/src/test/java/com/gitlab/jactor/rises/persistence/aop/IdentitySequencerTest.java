package com.gitlab.jactor.rises.persistence.aop;

import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.test.extension.validate.fields.FieldValue;
import com.gitlab.jactor.rises.test.extension.validate.fields.RequiredFieldsExtension;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("An IdentitySequencer")
class IdentitySequencerTest {

    private IdentitySequencer identitySequencer = new IdentitySequencer();

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            AddressEntity.class, asList(
                    new FieldValue("addressLine1", "Test Boulevard 1"),
                    new FieldValue("zipCode", 1001),
                    new FieldValue("city", "Testing")
            ), GuestBookEntryEntity.class, asList(
                    new FieldValue("entry", "jibberish"),
                    new FieldValue("name", "McTest"),
                    new FieldValue("guestBook", () -> aGuestBook().build())
            ), GuestBookEntity.class, asList(
                    new FieldValue("title", "my guestbook"),
                    new FieldValue("user", () -> aUser().build())
            ), UserEntity.class, asList(
                    new FieldValue("username", "supreme"),
                    new FieldValue("personEntity", () -> aPerson().build())
            ), PersonEntity.class, asList(
                    new FieldValue("addressEntity", () -> anAddress().build()),
                    new FieldValue("surname", "sure, man")
            )
    ));

    @Mock private JoinPoint joinPointMock;

    @BeforeEach void setUpMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should increment the sequence of an entity, and the first number should be 1,000,000")
    @Test void shouldIncrementSequenceOfAddressEntity() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{anAddress().build()}, new Object[]{anAddress().build()}, new Object[]{anAddress().build()});

        PersistentEntity first = (PersistentEntity) identitySequencer.addIdentity(joinPointMock);
        PersistentEntity second = (PersistentEntity) identitySequencer.addIdentity(joinPointMock);
        PersistentEntity third = (PersistentEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(first.getId()).as("first").isEqualTo(1000000L),
                () -> assertThat(second.getId()).as("second").isEqualTo(1000001L),
                () -> assertThat(third.getId()).as("third").isEqualTo(1000002L)
        );
    }

    @DisplayName("should set id on an person entity and address entity as well as the user entity")
    @Test void shouldSetIdOnPersonsAddressAndUser() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aPerson().with(aUser()).build()});

        PersonEntity person = (PersonEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(person.getId()).as("person.id").isEqualTo(1000000L),
                () -> assertThat(person.getAddressEntity().getId()).as("person.address.id").isEqualTo(1000000L),
                () -> assertThat(person.getUserEntity().getId()).as("person.user.id").isEqualTo(1000000L)
        );
    }

    @DisplayName("should set id on an user entity as well as person and address")
    @Test void shouldSetIdOnUserPersonAndAddress() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aUser().build()});

        UserEntity user = (UserEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(user.getId()).as("user.id").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getId()).as("user.person.id").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getAddressEntity().getId()).as("user.person.address.id").isEqualTo(1000000L)
        );
    }

    @DisplayName("should set id on a guestBook entity, as well as user, person, and address")
    @Test void shouldSaveGuestBookUserPersonAndAddress() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aGuestBook().build()});

        GuestBookEntity guestBook = (GuestBookEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(guestBook.getId()).as("guestBook.id").isEqualTo(1000000L),
                () -> assertThat(guestBook.getUser().getId()).as("guestBook.user.id").isEqualTo(1000000L),
                () -> assertThat(guestBook.getUser().getPerson().getId()).as("guestBook.user.person.id").isEqualTo(1000000L),
                () -> assertThat(guestBook.getUser().getPerson().getAddressEntity().getId()).as("guestBook.user.person.address.id").isEqualTo(1000000L)
        );
    }
}
