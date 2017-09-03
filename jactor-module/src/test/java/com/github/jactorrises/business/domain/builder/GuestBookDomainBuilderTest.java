package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.GuestBookDomain;
import com.github.jactorrises.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The GuestBookDomainBuilder")
class GuestBookDomainBuilderTest {

    @DisplayName("should not build an instance without the title of the guest book")
    @Test void willNotBuildGuestBookWithoutTheTitleOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().with(new UserEntity()).build())
                .withMessage(GuestBookDomainBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance with an empty title for the guest book")
    @Test void willNotBuildGuestBookWithAnEmptyTitleOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitleAs("").with(new UserEntity()).build())
                .withMessage(GuestBookDomainBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance without an owner of the guest book")
    @Test void willNotBuildGuestBookWithoutTheUserWhoIsTheOwnerOfTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> GuestBookDomain.aGuestBook().withTitleAs("some title").build())
                .withMessage(GuestBookDomainBuilder.THE_GUEST_BOOK_MUST_BELONG_TO_A_USER);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildGuestBookWhenAllRequiredFieldsAreSet() throws Exception {
        assertThat(GuestBookDomain.aGuestBook().withTitleAs("some title").with(new UserEntity()).build()).isNotNull();
    }
}
