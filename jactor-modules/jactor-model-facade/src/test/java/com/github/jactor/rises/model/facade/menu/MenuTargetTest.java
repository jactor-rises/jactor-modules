package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A MenuTarget")
class MenuTargetTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void whenInvokingHashCodeTheResultShouldBeEqualOnDifferentInstancesThatAreEqual() {
        MenuTarget base = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));
        MenuTarget equal = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));
        MenuTarget notEqual = new MenuTarget(new MenuItemTarget("on target"), new Name("a menu"));

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void whenChecksForEqualityIsDoneTheValuesOfThePropertiesMustBeCorrect() {
        MenuTarget base = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));
        MenuTarget equal = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));
        MenuTarget notEqual = new MenuTarget(new MenuItemTarget("on target"), new Name("a menu"));

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
        MenuTarget menuTarget = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));
        assertThat(menuTarget.toString()).isEqualTo("MenuTarget[Name[a menu],a target]");
    }

    @DisplayName("should have the name of the menu and the menu item target of that menu")
    @Test void willEncapsulateTheNameOfTheMenuAndTheTargetOnTheMenu() {
        MenuTarget menuTarget = new MenuTarget(new MenuItemTarget("a target"), new Name("a menu"));

        assertAll(
                () -> assertThat(menuTarget.getMenuName().asString()).as("menu name").isEqualTo("a menu"),
                () -> assertThat(menuTarget.getMenuItemTarget().getTarget()).as("menu item target").isEqualTo("a target")
        );
    }
}
