package com.github.jactor.rises.model.domain.blog;

import com.github.jactor.rises.model.domain.user.UserDomain;
import com.github.jactor.rises.test.extension.SuppressValidInstanceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The BlogBuilder")
@ExtendWith(SuppressValidInstanceExtension.class)
class BlogBuilderTest {

    @BeforeEach
    void validateBlogBuild() {
        SuppressValidInstanceExtension.suppressFor(UserDomain.class);
    }

    @DisplayName("should not build a blog without a title")
    @Test void skalIkkeByggeUtenTittel() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().with(aUser()).build())
                .withMessageContaining("title").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog with an empty title")
    @Test void skalIkkeByggeMedTomTittel() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().with(aUser()).withTitleAs("").build())
                .withMessageContaining("title").withMessageContaining("has no value");
    }

    @DisplayName("should not build a blog without a user")
    @Test void skalIkkeByggeUtenBruker() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().withTitleAs("the title").build())
                .withMessageContaining("user").withMessageContaining("has no value");
    }

    @DisplayName("should build a blog with a user and a title")
    @Test void skalByggeMedTittelOgBruker() {
        assertThat(BlogDomain.aBlog().withTitleAs("title").with(aUser()).build()).isNotNull();
    }
}
