package com.github.jactorrises.persistence.orm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultGuestBookEntity")
class DefaultGuestBookEntityTest {

    private DefaultGuestBookEntity defaultGuestBookEntityToTest;

    @BeforeEach void initClassToTest() {
        defaultGuestBookEntityToTest = new DefaultGuestBookEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultGuestBookEntity base = defaultGuestBookEntityToTest;
        base.setTitle("title");
        base.setUser(new DefaultUserEntity());

        DefaultGuestBookEntity equal = new DefaultGuestBookEntity(base);

        DefaultGuestBookEntity notEqual = new DefaultGuestBookEntity();
        notEqual.setTitle("another title");
        notEqual.setUser(new DefaultUserEntity());


        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultGuestBookEntity base = defaultGuestBookEntityToTest;
        base.setTitle("title");
        base.setUser(new DefaultUserEntity());

        DefaultGuestBookEntity equal = new DefaultGuestBookEntity(base);

        DefaultGuestBookEntity notEqual = new DefaultGuestBookEntity();
        notEqual.setTitle("another title");
        notEqual.setUser(new DefaultUserEntity());

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
