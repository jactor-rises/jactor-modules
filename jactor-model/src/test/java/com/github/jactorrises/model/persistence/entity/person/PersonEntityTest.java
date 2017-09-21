package com.github.jactorrises.model.persistence.entity.person;


import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.model.persistence.entity.address.AddressEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.github.jactorrises.model.persistence.entity.user.UserEntity.aUser;
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
        base.setFirstName(new Name("ola"));
        base.setLastName(new Name("norman"));
        base.setUserEntity(aUser().build());
        base.setLocale(new Locale("no"));

        PersonEntity equal = new PersonEntity(personEntityToTest);

        PersonEntity notEqual = new PersonEntity();
        notEqual.setAddressEntity(new AddressEntity());
        notEqual.setDescription("some description");
        notEqual.setFirstName(new Name("ola"));
        notEqual.setLastName(new Name("norman"));
        notEqual.setUserEntity(aUser().build());
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()").isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()").isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value").isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        PersonEntity base = personEntityToTest;
        base.setAddressEntity(new AddressEntity());
        base.setDescription("some description");
        base.setFirstName(new Name("ola"));
        base.setLastName(new Name("norman"));
        base.setUserEntity(aUser().build());
        base.setLocale(new Locale("no"));

        PersonEntity equal = new PersonEntity(personEntityToTest);

        PersonEntity notEqual = new PersonEntity();
        notEqual.setAddressEntity(new AddressEntity());
        notEqual.setDescription("some description");
        notEqual.setFirstName(new Name("ola"));
        notEqual.setLastName(new Name("norman"));
        notEqual.setUserEntity(aUser().build());
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal").isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual").isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
