package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.facade.PersistentDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.business.domain.GuestBookEntryDomain.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A GuestBookEntryDomainBuilder")
class GuestBookEntryDomainBuilderTest {

    @DisplayName("should not initialize a guest book entry without an entry")
    @Test void willNotBuildGuestBookEntryWithoutAnEntry() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aGuestBookEntry().with(aGuestBook()).withEntryAs(null, "aCreator").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize a guest book entry with an empty entry")
    @Test void willNotBuildGuestBookEntryWithAnEmptyEntry() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aGuestBookEntry().withEntryAs("", "guestName").with(aGuestBook()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize a guest book entry without the guest book")
    @Test void willNotBuildGuestBookEntryWithoutTheGuestBook() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aGuestBookEntry().withEntryAs("some entry", "guestName").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(GuestBookEntryDomainBuilder.THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK);
    }

    @DisplayName("should not initialize a guest book entry when all requred fields are set")
    @Test void willBuildGuestBookEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aGuestBookEntry().withEntryAs("some entry", "guestName").with(aGuestBook()).build())
                .isNotNull();
    }

    private GuestBookEntity aGuestBook() {
        return PersistentDataService.getInstance().provideInstanceFor(GuestBookEntity.class);
    }
}
