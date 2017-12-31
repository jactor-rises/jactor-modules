package com.github.jactorrises.persistence.entity.guestbook;

import com.github.jactorrises.test.extension.NowAsPureDateExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntryOrm")
class GuestBookEntryOrmTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        GuestBookEntryOrm base = aGuestBookEntry()
                .with(new GuestBookEntity())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        GuestBookEntryOrm equal = base.copy();

        GuestBookEntryOrm notEqual = aGuestBookEntry()
                .with(new GuestBookEntity())
                .withCreatorName("some other creator")
                .withEntry("some other entry")
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
        GuestBookEntryOrm base = aGuestBookEntry()
                .with(new GuestBookEntity())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        GuestBookEntryOrm equal = base.copy();

        GuestBookEntryOrm notEqual = aGuestBookEntry()
                .with(new GuestBookEntity())
                .withCreatorName("some other creator")
                .withEntry("some other entry")
                .build();

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should set created time when initialized")
    @ExtendWith(NowAsPureDateExtension.class)
    @Test void willHaveCreationTimeOnEntryWhenCreated() {
        assertThat(new GuestBookEntryOrm().getCreatedTime()).isEqualTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        GuestBookEntryOrm guestBookEntryEntityToTest = aGuestBookEntry()
                .withCreatorName("jactor")
                .withEntry("hi")
                .build();

        assertThat(guestBookEntryEntityToTest.toString())
                .contains("jactor")
                .contains("hi")
                .contains(LocalDate.now().toString());
    }
}
