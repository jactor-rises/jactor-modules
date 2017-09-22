package com.github.jactorrises.model.persistence.entity.person;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactorrises.model.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactorrises.model.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonEntity")
class PersonEntityTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        PersonEntity base = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withLastName("norman")
                .withLocale("no")
                .build();

        PersonEntity equal = new PersonEntity(base);

        PersonEntity notEqual = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withLastName("norman")
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
                .withLastName("norman")
                .withLocale("no")
                .build();

        PersonEntity equal = new PersonEntity(base);

        PersonEntity notEqual = aPerson()
                .with(anAddress())
                .with(aUser())
                .withDescription("some description")
                .withFirstName("ola")
                .withLastName("norman")
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
