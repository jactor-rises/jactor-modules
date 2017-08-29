package com.github.jactorrises.persistence.orm.domain;

import com.github.jactorrises.persistence.client.BlogEntryEntity;
import com.github.jactorrises.persistence.orm.time.NowAsPureDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultBlogEntryEntity")
class DefaultBlogEntryEntityTest {
    private DefaultBlogEntryEntity blogEntryEntityToTest;

    @BeforeEach void initBlogEntryEntity() {
        NowAsPureDate.set();
        blogEntryEntityToTest = new DefaultBlogEntryEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultBlogEntryEntity base = blogEntryEntityToTest;
        base.setBlog(new DefaultBlogEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        BlogEntryEntity equal = new DefaultBlogEntryEntity(blogEntryEntityToTest);

        BlogEntryEntity notEqual = new DefaultBlogEntryEntity();
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");
        notEqual.setBlog(new DefaultBlogEntity());

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultBlogEntryEntity base = blogEntryEntityToTest;
        base.setBlog(new DefaultBlogEntity());
        base.setEntry("some entry");
        base.setCreatorName("some creator");

        BlogEntryEntity equal = new DefaultBlogEntryEntity(blogEntryEntityToTest);

        BlogEntryEntity notEqual = new DefaultBlogEntryEntity();
        notEqual.setEntry("some other entry");
        notEqual.setCreatorName("some other creator");
        notEqual.setBlog(new DefaultBlogEntity());

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
