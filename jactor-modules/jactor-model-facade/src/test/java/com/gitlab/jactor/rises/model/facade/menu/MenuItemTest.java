package com.gitlab.jactor.rises.model.facade.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gitlab.jactor.rises.model.facade.menu.MenuItem.aMenuItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A MenuItem")
class MenuItemTest {

    private String target = "hit?dead=center";

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        MenuItem base = aMenuItem().withTarget("target?some=parameter")
                .add(aMenuItem().withTarget("childTarget?child=parameter")).build();
        MenuItem equal = aMenuItem().withTarget("target?some=parameter")
                .add(aMenuItem().withTarget("childTarget?child=parameter")).build();
        MenuItem notEqual = aMenuItem().withTarget("target?another=parameter").build();

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        MenuItem base = aMenuItem().withTarget("target?some=parameter")
                .add(aMenuItem().withTarget("childTarget?child=parameter")).build();
        MenuItem equal = aMenuItem().withTarget("target?some=parameter")
                .add(aMenuItem().withTarget("childTarget?child=parameter")).build();
        MenuItem notEqual = aMenuItem().withTarget("target?another=parameter").build();

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should not be chosen when the target is unknown")
    @Test void shouldNotBeChosenWhenTheTargetIsUnknown() {
        MenuItem testMenuItem = aMenuItem().withTarget("miss").build();
        assertThat(testMenuItem.isChosen(target)).isEqualTo(false);
    }

    @DisplayName("should be chosen when the target is known")
    @Test void shouldBeChosenWhenTheTargetIsKnown() {
        MenuItem testMenuItem = aMenuItem().withTarget("hit?dead=center").build();
        assertThat(testMenuItem.isChosen(target)).isEqualTo(true);
    }

    @DisplayName("should not have chosen child when the target is unknown")
    @Test void shouldNotBeChosenChildWhenMenuTargetOnChildIsUnknown() {
        MenuItem testMenuItem = aMenuItem().withTarget("hit?dead=center")
                .add(aMenuItem().withTarget("miss"))
                .build();

        assertThat(testMenuItem.isChildChosen(target)).isEqualTo(false);
    }

    @DisplayName("should have chosen child when the target is known")
    @Test void shouldBeChosenChildWhenMenuTargetOnChildIsKnown() {
        MenuItem testMenuItem = aMenuItem().withTarget("miss")
                .add(aMenuItem().withTarget("hit?dead=center"))
                .build();

        assertThat(testMenuItem.isChildChosen(target)).isEqualTo(true);
    }
}
