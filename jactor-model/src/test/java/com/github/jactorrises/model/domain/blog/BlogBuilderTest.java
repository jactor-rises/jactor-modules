package com.github.jactorrises.model.domain.blog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.persistence.builder.UserEntityBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The BlogBuilder")
class BlogBuilderTest {

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
                .withMessageContaining("user").withMessageContaining("must be present");
    }

    @DisplayName("should build a blog with a user and a title")
    @Test void skalByggeMedTittelOgBruker() {
        assertThat(BlogDomain.aBlog().withTitleAs("title").with(aUser()).build()).isNotNull();
    }
}
