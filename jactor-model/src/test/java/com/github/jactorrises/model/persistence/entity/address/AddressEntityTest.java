package com.github.jactorrises.model.persistence.entity.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("An AddressEntity")
class AddressEntityTest {

    private AddressEntity addressEntityToTest;

    @BeforeEach void initAddressEntityImpl() {
        addressEntityToTest = new AddressEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        AddressEntity base = addressEntityToTest;
        base.setAddressLine1("somewhere");
        base.setZipCode(1234);
        base.setCountry("NO");
        base.setCity("some city");
        base.setAddressLine2("somewhere else");
        base.setAddressLine3("way out there");

        AddressEntity equal = new AddressEntity(base);

        AddressEntity notEqual = new AddressEntity();
        notEqual.setAddressLine1("somewhere else");
        notEqual.setZipCode(5678);
        notEqual.setCountry("SE");
        notEqual.setCity("some other city");
        notEqual.setAddressLine2("some place");
        notEqual.setAddressLine3("in the distance");

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        AddressEntity base = addressEntityToTest;
        base.setAddressLine1("somewhere");
        base.setZipCode(1234);
        base.setCountry("NO");
        base.setCity("some city");
        base.setAddressLine2("somewhere else");
        base.setAddressLine3("way out there");

        AddressEntity equal = new AddressEntity(addressEntityToTest);

        AddressEntity notEqual = new AddressEntity();
        notEqual.setAddressLine1("somewhere");
        notEqual.setZipCode(1234);
        notEqual.setCountry("NO");
        notEqual.setCity("some city");
        notEqual.setAddressLine2("somewhere else");
        notEqual.setAddressLine3("inside");

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
