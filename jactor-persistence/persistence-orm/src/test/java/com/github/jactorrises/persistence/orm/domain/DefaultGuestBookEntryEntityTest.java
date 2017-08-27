package com.github.jactorrises.persistence.orm.domain;

import com.github.jactorrises.persistence.orm.time.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultGuestBookEntity")
class DefaultGuestBookEntryEntityTest {

    private DefaultGuestBookEntryEntity defaultGuestBookEntryEntityToTest;

    @BeforeEach void initEntryForTestingWithNowAsPureDate() {
        NowAsPureDate.set();
        defaultGuestBookEntryEntityToTest = new DefaultGuestBookEntryEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultGuestBookEntryEntity base = defaultGuestBookEntryEntityToTest;
        base.setGuestBook(new DefaultGuestBookEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        DefaultGuestBookEntryEntity equal = new DefaultGuestBookEntryEntity(base);

        DefaultGuestBookEntryEntity notEqual = new DefaultGuestBookEntryEntity();
        notEqual.setGuestBook(new DefaultGuestBookEntity());
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultGuestBookEntryEntity base = defaultGuestBookEntryEntityToTest;
        base.setGuestBook(new DefaultGuestBookEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        DefaultGuestBookEntryEntity equal = new DefaultGuestBookEntryEntity(base);

        DefaultGuestBookEntryEntity notEqual = new DefaultGuestBookEntryEntity();
        notEqual.setGuestBook(new DefaultGuestBookEntity());
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should set created time when initialized")
    @Test void willHaveCreationTimeOnEntryWhenCreated() {
        assertThat(defaultGuestBookEntryEntityToTest.getCreatedTime()).isEqualTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        defaultGuestBookEntryEntityToTest.setCreatorName("jactor");
        defaultGuestBookEntryEntityToTest.setEntry("hi");

        assertThat(defaultGuestBookEntryEntityToTest.toString())
                .contains("jactor")
                .contains("hi")
                .contains(LocalDate.now().toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
