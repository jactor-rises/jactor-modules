package com.github.jactorrises.model.domain.guestbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The GuestBookBuilder")
class GuestBookBuilderTest {

    @DisplayName("should not build an instance without the title of the guest book")
    @Test void willNotBuildGuestBookWithoutTheTitleOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().with(aUser()).build())
                .withMessage(GuestBookBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance with an empty title for the guest book")
    @Test void willNotBuildGuestBookWithAnEmptyTitleOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitle("").with(aUser()).build())
                .withMessage(GuestBookBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance without an owner of the guest book")
    @Test void willNotBuildGuestBookWithoutTheUserWhoIsTheOwnerOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitle("some title").build())
                .withMessage(GuestBookBuilder.THE_GUEST_BOOK_MUST_BELONG_TO_A_USER);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildGuestBookWhenAllRequiredFieldsAreSet() throws Exception {
        assertThat(GuestBookDomain.aGuestBook().withTitle("some title").with(aUser()).build()).isNotNull();
    }
}
