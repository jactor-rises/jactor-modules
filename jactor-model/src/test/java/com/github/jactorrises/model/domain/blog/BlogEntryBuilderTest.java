package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.blog.BlogEntryDomain.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The BlogEntryBuilder")
class BlogEntryBuilderTest {

    @DisplayName("should not build a blog entry without the entry")
    @Test void willNotBuildBlogEntryWithoutTheEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry(null).withCreatorName("aCreator").with(BlogEntityBuilder.aBlog()).build())
                .withMessageContaining("entry").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog entry with an empty entry")
    @Test void willNotBuildBlogEntryWithAnEmptyEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("").withCreatorName("aCreator").with(BlogEntityBuilder.aBlog()).build())
                .withMessageContaining("entry").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog entry without a blog")
    @Test void willNotBuildBlogEntryWithoutTheBlog() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("some entry").withCreatorName("aCreator").build())
                .withMessageContaining("blog").withMessageContaining("must be present");
    }

    @DisplayName("should not build a blog entry without the creator")
    @Test void willNotBuildBlogEntryWithoutTheCreator() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("some entry").withCreatorName(null).with(BlogEntityBuilder.aBlog()).build())
                .withMessageContaining("creatorName").withMessageContaining("must be present");
    }

    @DisplayName("should build a blog entry when all required fields are set")
    @Test void willBuildBlogEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aBlogEntry().with(BlogEntityBuilder.aBlog()).withEntry("some entry").withCreatorName("aCreator").build()).isNotNull();
    }

}
