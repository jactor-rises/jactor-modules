package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.test.extension.NowAsPureDateExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A BlogEntryOrm")
@ExtendWith(NowAsPureDateExtension.class)
class BlogEntryOrmTest {

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        BlogEntryOrm base = aBlogEntry()
                .with(new BlogOrm())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        BlogEntryOrm equal = base.copy();

        BlogEntryOrm notEqual = aBlogEntry()
                .with(new BlogOrm())
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
        BlogEntryOrm base = aBlogEntry()
                .with(new BlogOrm())
                .withCreatorName("some creator")
                .withEntry("some entry")
                .build();

        BlogEntryOrm equal = base.copy();

        BlogEntryOrm notEqual = aBlogEntry()
                .with(new BlogOrm())
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
