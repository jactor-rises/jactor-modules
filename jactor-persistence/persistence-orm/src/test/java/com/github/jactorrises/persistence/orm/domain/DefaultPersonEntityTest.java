package com.github.jactorrises.persistence.orm.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultPersontEntity")
class DefaultPersonEntityTest {

    private DefaultPersonEntity defaultPersonEntityToTest;

    @BeforeEach void initDefaultPersonEntity() {
        defaultPersonEntityToTest = new DefaultPersonEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultPersonEntity base = defaultPersonEntityToTest;
        base.setAddressEntity(new DefaultAddressEntity());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new DefaultUserEntity());
        base.setLocale(new Locale("no"));

        DefaultPersonEntity equal = new DefaultPersonEntity(defaultPersonEntityToTest);

        DefaultPersonEntity notEqual = new DefaultPersonEntity();
        notEqual.setAddressEntity(new DefaultAddressEntity());
        notEqual.setDescription("some other description");
        notEqual.setFirstName("kari");
        notEqual.setLastName("norman");
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultPersonEntity base = defaultPersonEntityToTest;
        base.setAddressEntity(new DefaultAddressEntity());
        base.setDescription("some description");
        base.setFirstName("ola");
        base.setLastName("norman");
        base.setUserEntity(new DefaultUserEntity());
        base.setLocale(new Locale("no"));

        DefaultPersonEntity equal = new DefaultPersonEntity(defaultPersonEntityToTest);

        DefaultPersonEntity notEqual = new DefaultPersonEntity();
        notEqual.setAddressEntity(new DefaultAddressEntity());
        notEqual.setDescription("some other description");
        notEqual.setFirstName("kari");
        notEqual.setLastName("norman");
        notEqual.setLocale(new Locale("se"));

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
