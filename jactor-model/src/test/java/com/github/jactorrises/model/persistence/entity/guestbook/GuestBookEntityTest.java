package com.github.jactorrises.model.persistence.entity.guestbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntity")
class GuestBookEntityTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        GuestBookEntity base = aGuestBook()
                .with(aUser().build())
                .withTitle("title")
                .build();

        GuestBookEntity equal = new GuestBookEntity(base);

        GuestBookEntity notEqual = aGuestBook()
                .with(aUser().build())
                .withTitle("another title")
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
        GuestBookEntity base = aGuestBook()
                .with(aUser().build())
                .withTitle("title")
                .build();

        GuestBookEntity equal = new GuestBookEntity(base);

        GuestBookEntity notEqual = aGuestBook()
                .with(aUser().build())
                .withTitle("another title")
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
