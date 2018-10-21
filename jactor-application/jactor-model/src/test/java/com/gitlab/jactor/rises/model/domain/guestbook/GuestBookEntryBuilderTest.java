package com.gitlab.jactor.rises.model.domain.guestbook;

import com.gitlab.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Set;

import static com.gitlab.jactor.rises.model.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.gitlab.jactor.rises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The GuestBookEntryBuilder")
@ExtendWith(SuppressValidInstanceExtension.class)
class GuestBookEntryBuilderTest {

    @RegisterExtension SuppressValidInstanceExtension suppressValidation = new SuppressValidInstanceExtension(
            Set.of(GuestBookDomain.class)
    );

    @DisplayName("should not initialize a guest book entry without an entry")
    @Test void willNotBuildGuestBookEntryWithoutAnEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().with(aGuestBook()).withEntry(null, "aCreator").build())
                .withMessageContaining("entry");
    }

    @DisplayName("should not initialize a guest book entry with an empty entry")
    @Test void willNotBuildGuestBookEntryWithAnEmptyEntry() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().withEntry("", "guestName").with(aGuestBook()).build())
                .withMessageContaining("entry");
    }

    @DisplayName("should not initialize a guest book entry without the guest book")
    @Test void willNotBuildGuestBookEntryWithoutTheGuestBook() {
        assertThatIllegalStateException().isThrownBy(() -> aGuestBookEntry().withEntry("some entry", "guestName").build())
                .withMessageContaining("guestBook");
    }

    @DisplayName("should initialize a guest book entry when all requred fields are set")
    @Test void willBuildGuestBookEntryWhenAllRequiredFieldsAreSet() {
        assertThat(aGuestBookEntry().withEntry("some entry", "guestName").with(aGuestBook()).build())
                .isNotNull();
    }
}
