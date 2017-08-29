package com.github.jactorrises.persistence.orm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaulBlogtEntity")
class DefaultUserEntityTest {

    private DefaultUserEntity defaultUserEntityToTest;

    @BeforeEach
    void initClassToTest() {
        defaultUserEntityToTest = new DefaultUserEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectImplementedHashCode() {
        DefaultUserEntity base = defaultUserEntityToTest;
        base.setUserName("some user");
        base.setPersonEntity(new DefaultPersonEntity());
        base.setEmailAddress("some@where");
        base.setUserNameAsEmailAddress();

        DefaultUserEntity equal = new DefaultUserEntity();
        equal.setUserName("some user");
        equal.setPersonEntity(new DefaultPersonEntity());
        equal.setEmailAddress("some@where");
        equal.setUserNameAsEmailAddress();

        DefaultUserEntity notEqual = new DefaultUserEntity();
        notEqual.setEmailAddress("any@where");
        notEqual.setPersonEntity(new DefaultPersonEntity());
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
        DefaultUserEntity base = defaultUserEntityToTest;
        base.setPersonEntity(new DefaultPersonEntity());
        base.setUserName("some user");
        base.setEmailAddress("some@where");
        base.setUserNameAsEmailAddress();

        DefaultUserEntity equal = new DefaultUserEntity(base);

        DefaultUserEntity notEqual = new DefaultUserEntity();
        notEqual.setPersonEntity(new DefaultPersonEntity());
        notEqual.setEmailAddress("any@where");
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
