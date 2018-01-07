package com.github.jactorrises.client.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("An EmailAddress")
class EmailAddressTest {

    @DisplayName("should have an implemention of the hashCode method")
    @Test void whenInvokingHashCodeTheImplementationShouldBeCorrect() {
        EmailAddress base = new EmailAddress("someone", "somewhere");
        EmailAddress equal = new EmailAddress("someone", "somewhere");
        EmailAddress notEqual = new EmailAddress("anyone", "anywhere");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void whenChecksForEqualityTheImplementationShouldBeCorrect() {
        EmailAddress base = new EmailAddress("someone", "somewhere");
        EmailAddress equal = new EmailAddress("someone", "somewhere");
        EmailAddress notEqual = new EmailAddress("anyone", "anywhere");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should not initialize without a prefix")
    @Test void willNotInitializeWhenPrefixIsNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> new EmailAddress(null, "somewhere"));
        assertThat(nullPointerException.getMessage()).isEqualTo("prefix cannot be empty");

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new EmailAddress("", "somewhere"));
        assertThat(illegalArgumentException.getMessage()).isEqualTo("prefix cannot be empty");
    }

    @DisplayName("should not initialize without a suffix")
    @Test void willNotInitializeWhenSuffixIsNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> new EmailAddress("someone", null));
        assertThat(nullPointerException.getMessage()).isEqualTo("suffix cannot be empty");

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new EmailAddress("someone", ""));
        assertThat(illegalArgumentException.getMessage()).isEqualTo("suffix cannot be empty");
    }

    @DisplayName("should be initialize with full email address")
    @Test void willInitializeWithFullEmailAddress() {
        assertThat(new EmailAddress("someone@somewhere")).isEqualTo(new EmailAddress("someone", "somewhere"));
    }

    @DisplayName("should not be initialized when the full email address is null or empty")
    @Test void willNotInitializeWhenFullEmailAddressIsNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> new EmailAddress(null));
        assertThat(nullPointerException.getMessage()).isEqualTo("full email address is required");

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new EmailAddress(""));
        assertThat(illegalArgumentException.getMessage()).isEqualTo("full email address is required");
    }

    @DisplayName("should not initialize with a full email address without an @-sign")
    @Test void willNotInitializeWithoutAtSignInConstructor() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new EmailAddress("someoneAreSomewhere"));
        assertThat(illegalArgumentException.getMessage()).isEqualTo("An email address requires an @-sign");
    }

    @DisplayName("should implement toString")
    @Test void whenInvokingToStringOnTheDataTypeItShouldBeImplementedOnTheDataTypeClass() {
        assertThat(new EmailAddress("someone", "somewhere").toString())
                .as("A toString should be implemented")
                .isEqualTo("EmailAddress[someone@somewhere]");
    }

    @DisplayName("should check if it is equal as given user name")
    @Test
    void shouldCheckIfEqualToUserName() {
        assertThat(new EmailAddress("way", "out.there").isSameAs(new UserName("way@out.there")))
                .as("An email address can be equal to user name")
                .isEqualTo(true);
    }
}
