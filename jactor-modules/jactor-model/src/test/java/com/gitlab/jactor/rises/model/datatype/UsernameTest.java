package com.gitlab.jactor.rises.model.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A Username")
class UsernameTest {

    @DisplayName("should be equal to another user name if the difference is only is case")
    @Test void willBeEqualIfOnlyDifferenceIsTheCharacterCase() {
        assertThat(new Username("jactor")).isEqualTo(new Username("JACTOR"));
    }

    @DisplayName("should have equal hashCode to another user name if the difference is only is case")
    @Test void willProduceEqualHashCodeIfOnlyDifferenceIsTheCharacterCase() {
        assertThat(new Username("jactor").hashCode()).isEqualTo(new Username("JACTOR").hashCode());
    }

    @DisplayName("should have an implementation of the hashCode method")
    @Test void willImplementHashCodeAccordingToTheJavaSpecifications() {
        Username base = new Username("someone");
        Username equal = new Username("SOMEONE");
        Username notEqual = new Username("SOMEONE else");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willImplementEqualsAccordingToTheJavaSpecifications() {
        Username base = new Username("someone");
        Username equal = new Username("SOMEONE");
        Username notEqual = new Username("SOMEONE else");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
