package com.github.jactorrises.persistence.boot.entity.guestbook;

import com.github.jactorrises.persistence.boot.entity.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RunWith(JUnitPlatform.class)
@DisplayName("A GuestBookEntityImpl")
class GuestBookEntryEntityImplTest {

    private GuestBookEntryEntityImpl guestBookEntryEntityImplToTest;

    @BeforeEach void initEntryForTestingWithNowAsPureDate() {
        NowAsPureDate.set();
        guestBookEntryEntityImplToTest = new GuestBookEntryEntityImpl();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        GuestBookEntryEntityImpl base = guestBookEntryEntityImplToTest;
        base.setGuestBook(new GuestBookEntityImpl());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        GuestBookEntryEntityImpl equal = new GuestBookEntryEntityImpl(base);

        GuestBookEntryEntityImpl notEqual = new GuestBookEntryEntityImpl();
        notEqual.setGuestBook(new GuestBookEntityImpl());
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
        GuestBookEntryEntityImpl base = guestBookEntryEntityImplToTest;
        base.setGuestBook(new GuestBookEntityImpl());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        GuestBookEntryEntityImpl equal = new GuestBookEntryEntityImpl(base);

        GuestBookEntryEntityImpl notEqual = new GuestBookEntryEntityImpl();
        notEqual.setGuestBook(new GuestBookEntityImpl());
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
        assertThat(guestBookEntryEntityImplToTest.getCreatedTime()).isEqualTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        guestBookEntryEntityImplToTest.setCreatorName("jactor");
        guestBookEntryEntityImplToTest.setEntry("hi");

        assertThat(guestBookEntryEntityImplToTest.toString())
                .contains("jactor")
                .contains("hi")
                .contains(LocalDate.now().toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
