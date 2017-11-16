package com.github.jactorrises.model.persistence.entity.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.persistence.entity.address.AddressEntityBuilder.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("An AddressOrm")
class AddressOrmTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        AddressOrm base = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("somewhere else")
                .withAddressLine3("way out there")
                .withZipCode(1234)
                .withCountryCode("NO")
                .withCity("some city")
                .build();

        AddressOrm equal = base.copy();

        AddressOrm notEqual = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("somewhere in the")
                .withAddressLine3("distance")
                .withZipCode(5678)
                .withCountryCode("SE")
                .withCity("some other city")
                .build();

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        AddressOrm base = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("somewhere else")
                .withAddressLine3("way out there")
                .withZipCode(1234)
                .withCountryCode("NO")
                .withCity("some city")
                .build();

        AddressOrm equal = base.copy();

        AddressOrm notEqual = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("somewhere place")
                .withAddressLine3("in the distance")
                .withZipCode(5678)
                .withCountryCode("SE")
                .withCity("some other city")
                .build();

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
