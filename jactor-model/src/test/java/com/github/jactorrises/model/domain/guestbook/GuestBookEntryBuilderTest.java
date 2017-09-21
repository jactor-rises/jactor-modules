package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The GuestBookEntryBuilder")
class GuestBookEntryBuilderTest {

    @DisplayName("should not initialize a guest book entry without an entry")
    @Test void willNotBuildGuestBookEntryWithoutAnEntry() {
        assertThatIllegalArgumentException().isThrownBy(() -> aGuestBookEntry().with(new GuestBookEntity()).withEntry(null, "aCreator").build())
                .withMessage(GuestBookEntryBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize a guest book entry with an empty entry")
    @Test void willNotBuildGuestBookEntryWithAnEmptyEntry() {
        assertThatIllegalArgumentException().isThrownBy(() -> aGuestBookEntry().withEntry("", "guestName").with(new GuestBookEntity()).build())
                .withMessage(GuestBookEntryBuilder.THE_ENTRY_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not initialize a guest book entry without the guest book")
    @Test void willNotBuildGuestBookEntryWithoutTheGuestBook() {
        assertThatIllegalArgumentException().isThrownBy(() -> aGuestBookEntry().withEntry("some entry", "guestName").build())
                .withMessage(GuestBookEntryBuilder.THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK);
    }

    @DisplayName("should not initialize a guest book entry when all requred fields are set")
    @Test void willBuildGuestBookEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aGuestBookEntry().withEntry("some entry", "guestName").with(new GuestBookEntity()).build())
                .isNotNull();
    }
}
