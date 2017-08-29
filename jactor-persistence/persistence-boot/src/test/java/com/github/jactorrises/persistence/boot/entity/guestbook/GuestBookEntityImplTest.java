package com.github.jactorrises.persistence.boot.entity.guestbook;

import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A GuestBookEntityImpl")
class GuestBookEntityImplTest {

    private GuestBookEntityImpl GuestBookEntityImplToTest;

    @BeforeEach void initClassToTest() {
        GuestBookEntityImplToTest = new GuestBookEntityImpl();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        GuestBookEntityImpl base = GuestBookEntityImplToTest;
        base.setTitle("title");
        base.setUser(new UserEntityImpl());

        GuestBookEntityImpl equal = new GuestBookEntityImpl(base);

        GuestBookEntityImpl notEqual = new GuestBookEntityImpl();
        notEqual.setTitle("another title");
        notEqual.setUser(new UserEntityImpl());


        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        GuestBookEntityImpl base = GuestBookEntityImplToTest;
        base.setTitle("title");
        base.setUser(new UserEntityImpl());

        GuestBookEntityImpl equal = new GuestBookEntityImpl(base);

        GuestBookEntityImpl notEqual = new GuestBookEntityImpl();
        notEqual.setTitle("another title");
        notEqual.setUser(new UserEntityImpl());

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
