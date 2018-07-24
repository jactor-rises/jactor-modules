package com.gitlab.jactor.rises.model.domain.blog;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gitlab.jactor.rises.model.domain.blog.BlogEntryDomain.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The BlogEntryBuilder")
class BlogEntryBuilderTest {

    @DisplayName("should not build a blog entry without the entry")
    @Test void willNotBuildBlogEntryWithoutTheEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry(null).withCreatorName("aCreator").with(new BlogDto()).build())
                .withMessageContaining("entry").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog entry with an empty entry")
    @Test void willNotBuildBlogEntryWithAnEmptyEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("").withCreatorName("aCreator").with(new BlogDto()).build())
                .withMessageContaining("entry").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog entry without a blog")
    @Test void willNotBuildBlogEntryWithoutTheBlog() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("some entry").withCreatorName("aCreator").build())
                .withMessageContaining("blog").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog entry without the creator")
    @Test void willNotBuildBlogEntryWithoutTheCreator() {
        assertThatIllegalStateException().isThrownBy(() -> aBlogEntry().withEntry("some entry").withCreatorName(null).with(new BlogDto()).build())
                .withMessageContaining("creatorName").withMessageContaining("has no value");
    }

    @DisplayName("should build a blog entry when all required fields are set")
    @Test void willBuildBlogEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aBlogEntry().with(new BlogDto()).withEntry("some entry").withCreatorName("aCreator").build()).isNotNull();
    }
}
