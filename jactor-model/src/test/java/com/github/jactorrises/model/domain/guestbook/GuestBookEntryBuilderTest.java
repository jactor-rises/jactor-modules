package com.github.jactorrises.model.domain.guestbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The GuestBookEntryBuilder")
class GuestBookEntryBuilderTest {

    @DisplayName("should not initialize a guest book entry without an entry")
    @Test void willNotBuildGuestBookEntryWithoutAnEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().with(aGuestBook()).withEntry(null, "aCreator").build())
                .withMessageContaining("entry").withMessageContaining("cannot be empty");
    }

    @DisplayName("should not initialize a guest book entry with an empty entry")
    @Test void willNotBuildGuestBookEntryWithAnEmptyEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().withEntry("", "guestName").with(aGuestBook()).build())
                .withMessageContaining("entry").withMessageContaining("cannot be empty");
    }

    @DisplayName("should not initialize a guest book entry without the guest book")
    @Test void willNotBuildGuestBookEntryWithoutTheGuestBook() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().withEntry("some entry", "guestName").build())
                .withMessageContaining("guestBook").withMessageContaining("cannot be null");
    }

    @DisplayName("should initialize a guest book entry when all requred fields are set")
    @Test void willBuildGuestBookEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aGuestBookEntry().withEntry("some entry", "guestName").with(aGuestBook()).build())
                .isNotNull();
    }
}
