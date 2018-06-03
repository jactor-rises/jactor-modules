package com.github.jactor.rises.persistence.orm.entity.blog;

import com.github.jactor.rises.test.extension.time.NowAsPureDateExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.jactor.rises.persistence.orm.entity.blog.BlogEntryEntity.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntryEntity")
@ExtendWith(NowAsPureDateExtension.class)
class BlogEntryEntityTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        BlogEntryEntity base = aBlogEntry()
                .with(new BlogEntity())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        BlogEntryEntity equal = base.copy();

        BlogEntryEntity notEqual = aBlogEntry()
                .with(new BlogEntity())
                .withCreatorName("some other creator")
                .withEntry("some other entry")
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
        BlogEntryEntity base = aBlogEntry()
                .with(new BlogEntity())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        BlogEntryEntity equal = base.copy();

        BlogEntryEntity notEqual = aBlogEntry()
                .with(new BlogEntity())
                .withCreatorName("some other creator")
                .withEntry("some other entry")
                .build();

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal").isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual").isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
