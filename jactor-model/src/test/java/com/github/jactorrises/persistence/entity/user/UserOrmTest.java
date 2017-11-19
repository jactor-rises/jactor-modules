package com.github.jactorrises.persistence.entity.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.persistence.entity.person.PersonEntityBuilder.aPerson;
import static com.github.jactorrises.persistence.entity.user.UserEntityBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A UserOrm")
class UserOrmTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectImplementedHashCode() {
        UserOrm base = (UserOrm) aUser()
                .with(aPerson())
                .withEmailAddress("some@where")
                .withUserName("some user")
                .build();

        UserOrm equal = base.copy();

        UserOrm notEqual = (UserOrm) aUser()
                .with(aPerson())
                .withEmailAddress("any@where")
                .withUserName("some other user")
                .build();

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()").isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()").isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value").isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test
    void willHaveCorrectImplementedEquals() {
        UserOrm base = (UserOrm) aUser()
                .with(aPerson())
                .withEmailAddress("some@where")
                .withUserName("some user")
                .build();

        UserOrm equal = base.copy();

        UserOrm notEqual = (UserOrm) aUser()
                .with(aPerson())
                .withEmailAddress("any@where")
                .withUserName("some other user")
                .build();

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal").isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual").isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should not fail when reading email address with null value")
    @Test
    void shouldGetEmailAddress() {
        UserOrm userOrm = new UserOrm();
        userOrm.setEmailAddress(null);

        assertThat(userOrm.getEmailAddress()).isNull();
    }
}
