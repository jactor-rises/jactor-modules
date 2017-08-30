package com.github.jactorrises.persistence.boot.entity.person;


import com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RunWith(JUnitPlatform.class)
@DisplayName("A PersonEntityImpl")
class PersonEntityImplTest {

    private PersonEntityImpl personEntityImplToTest;

    @BeforeEach void initPersonEntityImpl() {
        personEntityImplToTest = new PersonEntityImpl();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        PersonEntityImpl base = personEntityImplToTest;
        base.setAddressEntity(new AddressEntityImpl());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new UserEntityImpl());
        base.setLocale(new Locale("no"));

        PersonEntityImpl equal = new PersonEntityImpl(personEntityImplToTest);

        PersonEntityImpl notEqual = new PersonEntityImpl();
        notEqual.setAddressEntity(new AddressEntityImpl());
        notEqual.setDescription("some description");
        notEqual.setFirstName("ola");
        notEqual.setLastName("norman");
        notEqual.setUserEntity(new UserEntityImpl());
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
        PersonEntityImpl base = personEntityImplToTest;
        base.setAddressEntity(new AddressEntityImpl());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new UserEntityImpl());
        base.setLocale(new Locale("no"));

        PersonEntityImpl equal = new PersonEntityImpl(personEntityImplToTest);

        PersonEntityImpl notEqual = new PersonEntityImpl();
        notEqual.setAddressEntity(new AddressEntityImpl());
        notEqual.setDescription("some description");
        notEqual.setFirstName("ola");
        notEqual.setLastName("norman");
        notEqual.setUserEntity(new UserEntityImpl());
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
