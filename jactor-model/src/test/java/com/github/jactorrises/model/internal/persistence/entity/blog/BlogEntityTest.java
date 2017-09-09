package com.github.jactorrises.model.internal.persistence.entity.blog;

import com.github.jactorrises.model.internal.persistence.entity.NowAsPureDate;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntity")
class BlogEntityTest {
    private BlogEntity blogEntityToTest;

    @BeforeEach void initDefaulBlogEntityToTestWithCreationTime() {
        NowAsPureDate.set();
        blogEntityToTest = new BlogEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        BlogEntity base = blogEntityToTest;

        base.setTitle("title");
        base.setUserEntity(new UserEntity());

        BlogEntity equal = new BlogEntity(base);
        equal.setTitle("title");
        equal.setUserEntity(new UserEntity());

        BlogEntity notEqual = new BlogEntity();
        notEqual.setTitle("another title");
        notEqual.setUserEntity(new UserEntity());

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        BlogEntity base = blogEntityToTest;

        base.setTitle("title");
        base.setUserEntity(new UserEntity());

        BlogEntity equal = new BlogEntity(base);

        BlogEntity notEqual = new BlogEntity();
        notEqual.setTitle("another title");
        notEqual.setUserEntity(new UserEntity());

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
        assertThat(blogEntityToTest.getCreated()).isEqualTo(LocalDate.now());
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        blogEntityToTest.setTitle("my blog");

        assertThat(blogEntityToTest.toString())
                .contains("BlogEntity")
                .contains("my blog")
                .contains(LocalDate.now().toString());
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
