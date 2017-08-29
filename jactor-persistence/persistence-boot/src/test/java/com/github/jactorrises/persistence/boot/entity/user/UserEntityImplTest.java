package com.github.jactorrises.persistence.boot.entity.user;

import com.github.jactorrises.persistence.boot.entity.person.PersonEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A UserEntityImpl")
class UserEntityImplTest {

    private UserEntityImpl userEntityImplToTest;

    @BeforeEach
    void initClassToTest() {
        userEntityImplToTest = new UserEntityImpl();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectImplementedHashCode() {
        UserEntityImpl base = userEntityImplToTest;
        base.setUserName("some user");
        base.setPersonEntity(new PersonEntityImpl());
        base.setEmailAddress("some@where");

        UserEntityImpl equal = new UserEntityImpl();
        equal.setUserName("some user");
        equal.setPersonEntity(new PersonEntityImpl());
        equal.setEmailAddress("some@where");

        UserEntityImpl notEqual = new UserEntityImpl();
        notEqual.setEmailAddress("any@where");
        notEqual.setPersonEntity(new PersonEntityImpl());
        notEqual.setUserName("some other user");

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test
    void willHaveCorrectImplementedEquals() {
        UserEntityImpl base = userEntityImplToTest;
        base.setPersonEntity(new PersonEntityImpl());
        base.setUserName("some user");
        base.setEmailAddress("some@where");

        UserEntityImpl equal = new UserEntityImpl(base);

        UserEntityImpl notEqual = new UserEntityImpl();
        notEqual.setPersonEntity(new PersonEntityImpl());
        notEqual.setEmailAddress("some@where");
        notEqual.setUserName("some other user");

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
