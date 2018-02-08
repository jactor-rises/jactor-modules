package com.github.jactorrises.persistence.orm.entity.blog;

import com.github.jactorrises.client.converter.FieldConverter;
import com.github.jactorrises.persistence.orm.entity.DateTextEmbeddable;
import com.github.jactor.rises.test.extension.NowAsPureDateExtension;
import com.github.jactor.rises.test.extension.SuppressValidInstanceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static com.github.jactorrises.persistence.orm.entity.blog.BlogEntity.aBlog;
import static com.github.jactorrises.persistence.orm.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntity")
@ExtendWith(NowAsPureDateExtension.class)
@ExtendWith(SuppressValidInstanceExtension.class)
class BlogEntityTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        BlogEntity base = aBlog()
                .with(aUser().build())
                .withTitle("title")
                .build();

        BlogEntity equal = base.copy();

        BlogEntity notEqual = aBlog()
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
        BlogEntity base = aBlog()
                .with(aUser().build())
                .withTitle("title")
                .build();

        BlogEntity equal = base.copy();

        BlogEntity notEqual = aBlog()
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
        assertThat(new BlogEntity().getCreated()).isEqualTo(FieldConverter.convert(LocalDate.now()));
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void shouldHaveAnImplementationOfTheToStringMethod() {
        BlogEntity BlogOrmToTest = aBlog()
                .with(aUser().build())
                .withTitle("my blog")
                .build();

        assertThat(BlogOrmToTest.toString())
                .contains("BlogEntity")
                .contains("my blog")
                .contains(new DateTextEmbeddable(LocalDate.now()).toString());
    }
}
