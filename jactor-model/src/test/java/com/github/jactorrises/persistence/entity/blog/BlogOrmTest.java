package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.DateTextEmbeddable;
import com.github.jactorrises.persistence.entity.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder.aBlog;
import static com.github.jactorrises.persistence.entity.user.UserEntityBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogOrm")
class BlogOrmTest {
    @BeforeEach void useNowAsPureDate() {
        NowAsPureDate.set();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        BlogOrm base = (BlogOrm) aBlog()
                .with(aUser().build())
                .withTitle("title")
                .build();

        BlogOrm equal = base.copy();

        BlogOrm notEqual = (BlogOrm) aBlog()
                .with(aUser().build())
                .withTitle("another title")
                .build();

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()").isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()").isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value").isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        BlogOrm base = (BlogOrm) aBlog()
                .with(aUser().build())
                .withTitle("title")
                .build();

        BlogOrm equal = base.copy();

        BlogOrm notEqual = (BlogOrm) aBlog()
                .with(aUser().build())
                .withTitle("another title")
                .build();

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
        assertThat(new BlogOrm().getCreated()).isEqualTo(LocalDate.now());
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        BlogOrm BlogOrmToTest = (BlogOrm) aBlog()
                .with(aUser().build())
                .withTitle("my blog")
                .build();

        assertThat(BlogOrmToTest.toString())
                .contains("BlogOrm")
                .contains("my blog")
                .contains(new DateTextEmbeddable(LocalDate.now()).toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
