package com.github.jactorrises.model.persistence.entity.guestbook;

import com.github.jactorrises.model.persistence.entity.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntryEntity")
class GuestBookEntryEntityTest {

    private GuestBookEntryEntity guestBookEntryEntityToTest;

    @BeforeEach void initEntryForTestingWithNowAsPureDate() {
        NowAsPureDate.set();
        guestBookEntryEntityToTest = new GuestBookEntryEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        GuestBookEntryEntity base = guestBookEntryEntityToTest;
        base.setGuestBook(new GuestBookEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        GuestBookEntryEntity equal = new GuestBookEntryEntity(base);

        GuestBookEntryEntity notEqual = new GuestBookEntryEntity();
        notEqual.setGuestBook(new GuestBookEntity());
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        GuestBookEntryEntity base = guestBookEntryEntityToTest;
        base.setGuestBook(new GuestBookEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        GuestBookEntryEntity equal = new GuestBookEntryEntity(base);

        GuestBookEntryEntity notEqual = new GuestBookEntryEntity();
        notEqual.setGuestBook(new GuestBookEntity());
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should set created time when initialized")
    @Test void willHaveCreationTimeOnEntryWhenCreated() {
        assertThat(guestBookEntryEntityToTest.getCreatedTime()).isEqualTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        guestBookEntryEntityToTest.setCreatorName("jactor");
        guestBookEntryEntityToTest.setEntry("hi");

        assertThat(guestBookEntryEntityToTest.toString())
                .contains("jactor")
                .contains("hi")
                .contains(LocalDate.now().toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
