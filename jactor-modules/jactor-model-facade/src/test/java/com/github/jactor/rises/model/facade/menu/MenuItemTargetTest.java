package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A MenuItemTarget")
class MenuItemTargetTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void whenInvokingHashCodeTheResultShouldBeEqualOnDifferentInstancesThatAreEqual() {
        MenuItemTarget base = new MenuItemTarget("target");
        MenuItemTarget equal = new MenuItemTarget("target");
        MenuItemTarget notEqual = new MenuItemTarget("another target");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void whenChecksForEqualityIsDoneTheValuesOfThePropertiesMustBeCorrect() {
        MenuItemTarget base = new MenuItemTarget("target");
        MenuItemTarget equal = new MenuItemTarget("target");
        MenuItemTarget notEqual = new MenuItemTarget("another target");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void whenInvokingToStringOnTheDataTypeItShouldBeImplementedOnTheDataTypeClass() {
        assertThat(new MenuItemTarget("hit?with=parameter").toString()).isEqualTo("hit?with=parameter");
    }

    @DisplayName("should not initialize with a target being null")
    @Test void whenInitializingTheTargetCannotBeNull() {
        assertThatNullPointerException().isThrownBy(() -> new MenuItemTarget((String) null))
                .withMessage(MenuItemTarget.THE_TARGET_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize with a target being null")
    @Test void whenInitializingTheTargetCannotBeEmpty() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> new MenuItemTarget("")).getMessage())
                .isEqualTo(MenuItemTarget.THE_TARGET_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not contain parameters without the question sign on the target")
    @Test void willNotContainParametersWhenTheTargetDoesNotContainQuestionMark() {
        MenuItemTarget menuItemTarget = new MenuItemTarget("targetparam=value");

        assertAll(
                () -> assertThat(menuItemTarget.getTarget()).as("target wthout ? is a target without parameters").isEqualTo("targetparam=value"),
                () -> assertThat(menuItemTarget.getParameters()).as("#s has parameters").isEmpty()
        );
    }

    @DisplayName("should contain a parameter when the target cotains a question mark")
    @Test void willContainOneParameterWhenTheTargetContainsQuestionMarkNameEqualValue() {
        MenuItemTarget menuItemTarget = new MenuItemTarget("target?param=value");
        Parameter parameter = menuItemTarget.getParameters().iterator().next();

        assertAll(
                () -> assertThat(menuItemTarget.getTarget()).as("target without parameters").isEqualTo("target"),
                () -> assertThat(parameter.getKey()).as("parameter name").isEqualTo("param"),
                () -> assertThat(parameter.getValue()).as("parameter value").isEqualTo("value")
        );
    }

    @DisplayName("should contain more than one parameter when the key=value pair is forllowed by second ofter a comma")
    @Test void willContainTwoParametersWhenTheTargetContainsTwoQuestionMarksWithNameEqualValueSeperatedByComma() {
        MenuItemTarget menuItemTarget = new MenuItemTarget("target?param=value,another=parameter");

        assertAll(
                () -> assertThat(menuItemTarget.getTarget()).as("target without parameters").isEqualTo("target"),
                () -> assertThat(menuItemTarget.getParameters()).as("parameters ofter the ? on the target")
                        .contains(new Parameter("param", "value"), new Parameter("another", "parameter"))
        );
    }
}
