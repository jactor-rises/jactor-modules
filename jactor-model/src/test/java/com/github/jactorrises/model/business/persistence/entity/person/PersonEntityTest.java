package com.github.jactorrises.model.business.persistence.entity.person;


import com.github.jactorrises.model.business.persistence.entity.address.AddressEntity;
import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonEntity")
class PersonEntityTest {

    private PersonEntity personEntityToTest;

    @BeforeEach void initPersonEntityImpl() {
        personEntityToTest = new PersonEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        PersonEntity base = personEntityToTest;
        base.setAddressEntity(new AddressEntity());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new UserEntity());
        base.setLocale(new Locale("no"));

        PersonEntity equal = new PersonEntity(personEntityToTest);

        PersonEntity notEqual = new PersonEntity();
        notEqual.setAddressEntity(new AddressEntity());
        notEqual.setDescription("some description");
        notEqual.setFirstName("ola");
        notEqual.setLastName("norman");
        notEqual.setUserEntity(new UserEntity());
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        PersonEntity base = personEntityToTest;
        base.setAddressEntity(new AddressEntity());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new UserEntity());
        base.setLocale(new Locale("no"));

        PersonEntity equal = new PersonEntity(personEntityToTest);

        PersonEntity notEqual = new PersonEntity();
        notEqual.setAddressEntity(new AddressEntity());
        notEqual.setDescription("some description");
        notEqual.setFirstName("ola");
        notEqual.setLastName("norman");
        notEqual.setUserEntity(new UserEntity());
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
