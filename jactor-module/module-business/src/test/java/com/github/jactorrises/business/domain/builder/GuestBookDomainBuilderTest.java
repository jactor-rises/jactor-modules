package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.GuestBookDomain;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.boot.facade.PersistentDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A GuestBookDomainBuilder")
class GuestBookDomainBuilderTest {

    @DisplayName("should not build an instance without the title of the guest book")
    @Test void willNotBuildGuestBookWithoutTheTitleOfTheGuestBook() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> GuestBookDomain.aGuestBook().with(aUser()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookDomainBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance with an empty title for the guest book")
    @Test void willNotBuildGuestBookWithAnEmptyTitleOfTheGuestBook() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> GuestBookDomain.aGuestBook().withTitleAs("").with(aUser()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookDomainBuilder.THE_TITLE_CANNOT_BE_BLANK);
    }

    @DisplayName("should not build an instance without an owner of the guest book")
    @Test void willNotBuildGuestBookWithoutTheUserWhoIsTheOwnerOfTheGuestBook() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> GuestBookDomain.aGuestBook().withTitleAs("some title").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookDomainBuilder.THE_GUEST_BOOK_MUST_BELONG_TO_A_USER);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildGuestBookWhenAllRequiredFieldsAreSet() throws Exception {
        assertThat(GuestBookDomain.aGuestBook().withTitleAs("some title").with(aUser()).build()).isNotNull();
    }

    private UserEntity aUser() {
        return PersistentDataService.getInstance().provideInstanceFor(UserEntity.class);
    }
}
