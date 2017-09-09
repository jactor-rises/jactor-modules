package com.github.jactorrises.model.internal.domain.blog;

import com.github.jactorrises.model.internal.domain.blog.BlogDomainBuilder;
import com.github.jactorrises.model.internal.domain.blog.BlogDomain;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The BlogDomainBuilder")
class BlogDomainBuilderTest {

    @DisplayName("should not build a blog without a title")
    @Test void skalIkkeByggeUtenTittel() {
        assertThatIllegalArgumentException().isThrownBy(() -> BlogDomain.aBlog().with(new UserEntity()).build())
                .withMessage(BlogDomainBuilder.THE_BLOG_MUST_HAVE_A_TITLE);
    }

    @DisplayName("should not build a blog with an empty title")
    @Test void skalIkkeByggeMedTomTittel() {
        assertThatIllegalArgumentException().isThrownBy(() -> BlogDomain.aBlog().with(new UserEntity()).withTitleAs("").build())
                .withMessage(BlogDomainBuilder.THE_BLOG_MUST_HAVE_A_TITLE);
    }

    @DisplayName("should not build a blog without a user")
    @Test void skalIkkeByggeUtenBruker() {
        assertThatIllegalArgumentException().isThrownBy(() -> BlogDomain.aBlog().withTitleAs("the title").build())
                .withMessage(BlogDomainBuilder.THE_BLOG_MUST_BELONG_TO_A_USER);
    }

    @DisplayName("should build a blog with a user and a title")
    @Test void skalByggeMedTittelOgBruker() {
        assertThat(BlogDomain.aBlog().withTitleAs("title").with(new UserEntity()).build()).isNotNull();
    }
}
