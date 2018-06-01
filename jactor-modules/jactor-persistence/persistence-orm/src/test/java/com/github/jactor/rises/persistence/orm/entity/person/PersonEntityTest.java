package com.github.jactor.rises.persistence.orm.entity.person;

import com.github.jactor.rises.persistence.orm.RequiredFieldsExtension;
import com.github.jactor.rises.test.extension.SuppressValidInstanceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.jactor.rises.persistence.orm.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.orm.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.orm.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonEntity")
@ExtendWith(RequiredFieldsExtension.class)
class PersonEntityTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        PersonEntity base = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withSurname("norman")
                .withLocale("no")
                .build();

        PersonEntity equal = base.copy();

        PersonEntity notEqual = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withSurname("norman")
                .withLocale("se")
                .build();

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()").isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()").isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value").isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        PersonEntity base = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withSurname("norman")
                .withLocale("no")
                .build();

        PersonEntity equal = base.copy();

        PersonEntity notEqual = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withSurname("norman")
                .withLocale("se")
                .build();

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal").isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual").isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
