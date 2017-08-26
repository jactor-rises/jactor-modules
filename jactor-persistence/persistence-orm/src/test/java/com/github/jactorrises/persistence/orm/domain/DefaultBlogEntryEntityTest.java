package nu.hjemme.persistence.orm.domain;

import nu.hjemme.persistence.client.BlogEntryEntity;
import nu.hjemme.persistence.orm.time.NowAsPureDate;
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
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
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
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @AfterEach void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
