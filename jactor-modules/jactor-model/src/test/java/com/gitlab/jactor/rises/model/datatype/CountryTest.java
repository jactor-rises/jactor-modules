package com.gitlab.jactor.rises.model.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A Country")
class CountryTest {

    @DisplayName("should not initialize when country code is not configured as ISO 3166")
    @Test void skalFeileNarInstansieringInneholderAndreLandkoderEnnFraISO3166() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new Country("illegal"));
        assertThat(illegalArgumentException.getMessage()).contains(Country.NOT_A_VALID_COUNTRY_CODE_ACCORDING_TO_ISO_3166);
    }

    @DisplayName("should initialize when country code is configured as ISO 3166")
    @Test void skalIkkeFeileNarInstansieringInneholderLandkoderEnnFraISO3166() {
        assertThat(new Country("NO")).isNotNull();
    }

    @DisplayName("should have an implemention of the hashCode method")
    @Test void whenInvokingHashCodeItShouldBeImplementedCorrect() {
        Country base = new Country("NO");
        Country equal = new Country("NO");
        Country notEqual = new Country("SE");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void whenInvokingEqualsItShouldBeImplementedCorrect() {
        Country base = new Country("NO");
        Country equal = new Country("NO");
        Country notEqual = new Country("SE");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should implement toString")
    @Test void whenInvokingToStringOnTheDataTypeItShouldBeImplementedOnTheDataTypeClass() {
        assertThat(new Country("NO").toString())
                .as("A toString should be implemented")
                .isEqualTo("Country[NO]");
    }

    @DisplayName("should not initialize when country code is null")
    @Test void skalFeileNarCountryCodeErNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> new Country(null));
        assertThat(nullPointerException.getMessage()).isEqualTo(Country.THE_COUNTRY_CODE_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize when couyntry code is empty")
    @Test void skalFeileNarCountryCodeErTom() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new Country(""));
        assertThat(illegalArgumentException.getMessage()).isEqualTo(Country.THE_COUNTRY_CODE_CANNOT_BE_EMPTY);
    }
}
