package nu.hjemme.business.domain.builder;

import nu.hjemme.persistence.client.BlogEntity;
import nu.hjemme.persistence.facade.PersistentDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nu.hjemme.business.domain.BlogEntryDomain.aBlogEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A BlogEntryBuilder")
class BlogEntryDomainBuilderTest {

    @DisplayName("should not build a blog entry without the entry")
    @Test void willNotBuildBlogEntryWithoutTheEntry() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aBlogEntry().withEntryAs(null, "aCreator").with(aBlog()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(BlogEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build a blog entry with an empty entry")
    @Test void willNotBuildBlogEntryWithAnEmptyEntry() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aBlogEntry().withEntryAs("", "aCreator").with(aBlog()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(BlogEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build a blog entry with a blog")
    @Test void willNotBuildBlogEntryWithoutTheBlog() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aBlogEntry().withEntryAs("some entry", "aCreatorName").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(BlogEntryDomainBuilder.THE_ENTRY_MUST_BELONG_TO_A_BLOG);
    }

    @DisplayName("should not build a blog entry with the creator")
    @Test void willNotBuildBlogEntryWithoutTheCreator() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aBlogEntry().withEntryAs("some entry", null).with(aBlog()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(BlogEntryDomainBuilder.THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE);
    }

    @DisplayName("should build a blog entry when all required fields are set")
    @Test void willBuildBlogEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aBlogEntry().with(aBlog()).withEntryAs("some entry", "aCreator").build()).isNotNull();
    }

    private BlogEntity aBlog() {
        return PersistentDataService.getInstance().provideInstanceFor(BlogEntity.class);
    }
}
