package com.github.jactorrises.model.domain.guestbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.persistence.builder.UserEntityBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The GuestBookBuilder")
class GuestBookBuilderTest {

    @DisplayName("should not build an instance without the title of the guest book")
    @Test void willNotBuildGuestBookWithoutTheTitleOfTheGuestBook() {
        assertThatIllegalStateException().isThrownBy(() -> GuestBookDomain.aGuestBook().with(aUser()).build())
                .withMessageContaining("title").withMessageContaining("has no value");
    }

    @DisplayName("should not build an instance with an empty title for the guest book")
    @Test void willNotBuildGuestBookWithAnEmptyTitleOfTheGuestBook() {
        assertThatIllegalStateException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitle("").with(aUser()).build())
                .withMessageContaining("title").withMessageContaining("has no value");
    }

    @DisplayName("should not build an instance without an owner of the guest book")
    @Test void willNotBuildGuestBookWithoutTheUserWhoIsTheOwnerOfTheGuestBook() {
        assertThatIllegalStateException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitle("some title").build())
                .withMessageContaining("user").withMessageContaining("must be present");
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildGuestBookWhenAllRequiredFieldsAreSet() {
        assertThat(GuestBookDomain.aGuestBook().withTitle("some title").with(aUser()).build()).isNotNull();
    }
}
