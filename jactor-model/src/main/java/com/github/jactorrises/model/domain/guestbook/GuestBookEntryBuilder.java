package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntityBuilder;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static java.util.Arrays.asList;

public final class GuestBookEntryBuilder extends Builder<GuestBookEntryDomain> {
    static final String THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK = "The entry must belong to a guest book";
    static final String THE_ENTRY_CANNOT_BE_EMPTY = "The entry cannot be empty";
    private static final String THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE = "The entry must be created by someone";

    private final GuestBookEntryEntityBuilder guestBookEntryEntityBuilder = aGuestBookEntry();

    GuestBookEntryBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getEntry()) ? Optional.empty() : Optional.of(THE_ENTRY_CANNOT_BE_EMPTY),
                domain -> domain.getGuestBook() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK),
                domain -> domain.getCreatorName() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE)
        ));
    }

    public GuestBookEntryBuilder withEntry(String entry, String guestName) {
        guestBookEntryEntityBuilder
                .withEntry(entry)
                .withCreatorName(guestName);

        return this;
    }

    public GuestBookEntryBuilder with(GuestBookEntity guestBookEntity) {
        guestBookEntryEntityBuilder.with(guestBookEntity);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookDomain guestBookDomain) {
        return with(guestBookDomain.getEntity());
    }

    public GuestBookEntryBuilder with(GuestBookEntityBuilder guestBookEntityBuilder) {
        return with(guestBookEntityBuilder.build());
    }

    @Override protected GuestBookEntryDomain buildBean() {
        return new GuestBookEntryDomain(guestBookEntryEntityBuilder.build());
    }

    public static GuestBookEntryDomain build(GuestBookEntryEntity guestBookEntryEntity) {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }
}
