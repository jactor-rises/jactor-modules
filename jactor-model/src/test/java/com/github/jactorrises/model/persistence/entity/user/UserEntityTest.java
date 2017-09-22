package com.github.jactorrises.model.persistence.entity.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.persistence.entity.person.PersonEntity.aPerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A UserEntity")
class UserEntityTest {

    private UserEntity userEntityToTest;

    @BeforeEach
    void initClassToTest() {
        userEntityToTest = new UserEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectImplementedHashCode() {
        UserEntity base = userEntityToTest;
        base.setUserName(new UserName("some user"));
        base.setPersonEntity(aPerson().build());
        base.setEmailAddress(new EmailAddress("some@where"));

        UserEntity equal = new UserEntity(base);

        UserEntity notEqual = new UserEntity();
        notEqual.setEmailAddress(new EmailAddress("any@where"));
        notEqual.setPersonEntity(aPerson().build());
        notEqual.setUserName(new UserName("some other user"));

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
        UserEntity base = userEntityToTest;
        base.setPersonEntity(aPerson().build());
        base.setUserName(new UserName("some user"));
        base.setEmailAddress(new EmailAddress("some@where"));

        UserEntity equal = new UserEntity(base);

        UserEntity notEqual = new UserEntity();
        notEqual.setPersonEntity(aPerson().build());
        equal.setEmailAddress(new EmailAddress("some@where"));
        notEqual.setUserName(new UserName("some other user"));

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should not fail when reading email address with null value")
    @Test
    void shouldGetEmailAddress() {
        userEntityToTest.setEmailAddress(null);
        assertThat(userEntityToTest.getEmailAddress()).isNull();
    }
}
