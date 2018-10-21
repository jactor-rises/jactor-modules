package com.gitlab.jactor.rises.model.domain.blog;

import com.gitlab.jactor.rises.model.domain.user.UserDomain;
import com.gitlab.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Set;

import static com.gitlab.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The BlogBuilder")
@ExtendWith(SuppressValidInstanceExtension.class)
class BlogBuilderTest {

    @RegisterExtension SuppressValidInstanceExtension suppressValidation = new SuppressValidInstanceExtension(
            Set.of(UserDomain.class)
    );

    @DisplayName("should not build a blog without a title")
    @Test void skalIkkeByggeUtenTittel() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().with(aUser()).build())
                .withMessageContaining("title");
    }

    @DisplayName("should not build a blog with an empty title")
    @Test void skalIkkeByggeMedTomTittel() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().with(aUser()).withTitle("").build())
                .withMessageContaining("title");
    }

    @DisplayName("should not build a blog without a user")
    @Test void skalIkkeByggeUtenBruker() {
        assertThatIllegalStateException().isThrownBy(() -> BlogDomain.aBlog().withTitle("the title").build())
                .withMessageContaining("user");
    }

    @DisplayName("should build a blog with a user and a title")
    @Test void skalByggeMedTittelOgBruker() {
        assertThat(BlogDomain.aBlog().withTitle("title").with(aUser()).build()).isNotNull();
    }
}
