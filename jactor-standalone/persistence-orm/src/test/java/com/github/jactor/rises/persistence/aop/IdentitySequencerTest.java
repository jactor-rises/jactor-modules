package com.github.jactor.rises.persistence.aop;

import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("A IdentitySequencer")
class IdentitySequencerTest {

    private IdentitySequencer identitySequencer = new IdentitySequencer();

    @Mock
    private JoinPoint joinPointMock;

    @BeforeEach
    void setUpMocks() {
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

    @DisplayName("should set id on an address entity, user entity as well as the person entity")
    @Test void shouldSetIdOnPersonsAddress() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aPerson().with(anAddress()).with(aUser().withUserName("someone")).build()});

        PersonEntity person = (PersonEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(person.getId()).as("person").isEqualTo(1000000L),
                () -> assertThat(person.getAddressEntity().getId()).as("address").isEqualTo(1000000L),
                () -> assertThat(person.getUserEntity().getId()).as("user").isEqualTo(1000000L)
        );
    }

    @DisplayName("should set id on an address entity as well as the person entity and user entity")
    @Test void shouldSetIdOnUserPersonAndAddress() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aUser().withUserName("someone").with(aPerson().with(anAddress())).build()});

        UserEntity user = (UserEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(user.getId()).as("user").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getId()).as("user").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getAddressEntity().getId()).as("address").isEqualTo(1000000L)
        );
    }

    @DisplayName("should set id on an user entity as well as the person entity")
    @Test void shouldSetIdOnUserAndPerson() {
        when(joinPointMock.getArgs()).thenReturn(new Object[]{aUser().withUserName("someone").with(aPerson().with(anAddress())).build()});

        UserEntity user = (UserEntity) identitySequencer.addIdentity(joinPointMock);

        assertAll(
                () -> assertThat(user.getId()).as("user").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getId()).as("user").isEqualTo(1000000L),
                () -> assertThat(user.getPerson().getAddressEntity().getId()).as("address").isEqualTo(1000000L)
        );
    }
}
