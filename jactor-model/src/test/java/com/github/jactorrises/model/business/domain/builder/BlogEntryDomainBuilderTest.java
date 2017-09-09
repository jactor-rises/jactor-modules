package com.github.jactorrises.model.business.domain.builder;

import com.github.jactorrises.model.business.persistence.entity.blog.BlogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.business.domain.BlogEntryDomain.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The BlogEntryBuilder")
class BlogEntryDomainBuilderTest {

    @DisplayName("should not build a blog entry without the entry")
    @Test void willNotBuildBlogEntryWithoutTheEntry() {
        assertThatIllegalArgumentException().isThrownBy(() -> aBlogEntry().withEntryAs(null, "aCreator").with(new BlogEntity()).build())
                .withMessage(BlogEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build a blog entry with an empty entry")
    @Test void willNotBuildBlogEntryWithAnEmptyEntry() {
        assertThatIllegalArgumentException().isThrownBy(() -> aBlogEntry().withEntryAs("", "aCreator").with(new BlogEntity()).build())
                .withMessage(BlogEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build a blog entry with a blog")
    @Test void willNotBuildBlogEntryWithoutTheBlog() {
        assertThatIllegalArgumentException().isThrownBy(() -> aBlogEntry().withEntryAs("some entry", "aCreatorName").build())
                .withMessage(BlogEntryDomainBuilder.THE_ENTRY_MUST_BELONG_TO_A_BLOG);
    }

    @DisplayName("should not build a blog entry with the creator")
    @Test void willNotBuildBlogEntryWithoutTheCreator() {
        assertThatIllegalArgumentException().isThrownBy(() -> aBlogEntry().withEntryAs("some entry", null).with(new BlogEntity()).build())
                .withMessage(BlogEntryDomainBuilder.THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE);
    }

    @DisplayName("should build a blog entry when all required fields are set")
    @Test void willBuildBlogEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aBlogEntry().with(new BlogEntity()).withEntryAs("some entry", "aCreator").build()).isNotNull();
    }

}
