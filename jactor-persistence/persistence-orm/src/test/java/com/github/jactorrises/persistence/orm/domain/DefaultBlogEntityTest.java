package com.github.jactorrises.persistence.orm.domain;

import com.github.jactorrises.persistence.orm.time.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaulBlogtEntity")
class DefaultBlogEntityTest {
    private DefaultBlogEntity defaultBlogEntityToTest;

    @BeforeEach void initDefaulBlogEntityToTestWithCreationTime() {
        NowAsPureDate.set();
        defaultBlogEntityToTest = new DefaultBlogEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultBlogEntity base = defaultBlogEntityToTest;

        base.setTitle("title");
        base.setUserEntity(new DefaultUserEntity());

        DefaultBlogEntity equal = new DefaultBlogEntity(base);
        equal.setTitle("title");
        equal.setUserEntity(new DefaultUserEntity());

        DefaultBlogEntity notEqual = new DefaultBlogEntity();
        notEqual.setTitle("another title");
        notEqual.setUserEntity(new DefaultUserEntity());

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultBlogEntity base = defaultBlogEntityToTest;

        base.setTitle("title");
        base.setUserEntity(new DefaultUserEntity());

        DefaultBlogEntity equal = new DefaultBlogEntity(base);

        DefaultBlogEntity notEqual = new DefaultBlogEntity();
        notEqual.setTitle("another title");
        notEqual.setUserEntity(new DefaultUserEntity());

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should set created when initialized")
    @Test void willSetCreatedWhenInitialized() {
        assertThat(defaultBlogEntityToTest.getCreated()).isEqualTo(LocalDate.now());
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        defaultBlogEntityToTest.setTitle("my blog");

        assertThat(defaultBlogEntityToTest.toString())
                .contains("DefaultBlogEntity")
                .contains("my blog")
                .contains(LocalDate.now().toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
