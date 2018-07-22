package com.gitlab.jactor.rises.model.datatype;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A Name")
class NameTest {
    @DisplayName("should have an implementation of the equals method")
    @Test void whenInvokingHashCodeTheResultShouldBeEqualOnDifferentInstancesThatAreEqual() {
        Name base = new Name("name");
        Name equal = new Name("name");
        Name notEqual = new Name("another name");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void whenChecksForEqualityIsDoneTheValuesOfThePropertiesMustBeCorrect() {
        Name base = new Name("name");
        Name equal = new Name("name");
        Name notEqual = new Name("another name");

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
        assertThat(new Name("another name").toString()).isEqualTo("Name[another name]");
    }

    @DisplayName("should not initialize when the name is null")
    @Test void whenInitializingTheNameCannotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> new Name(null)).withMessage(Name.A_NAME_MUST_BE_GIVEN);
    }

    @DisplayName("should not initialize when the name is empty")
    @Test void whenInitializingTheNameCannotBeEmpty() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Name("")).withMessage(Name.A_NAME_MUST_BE_GIVEN);
    }

    @DisplayName("should be sorted according to its value")
    @Test void willBeComparableAccordingToTheStringValue() {
        List<Name> names = new ArrayList<>(asList(new Name("Donald"), new Name("Ashley"), new Name("Bill")));
        Collections.sort(names);

        Assertions.assertThat(names).as("#s should be sorted").isEqualTo(asList(new Name("Ashley"), new Name("Bill"), new Name("Donald")));
    }
}
