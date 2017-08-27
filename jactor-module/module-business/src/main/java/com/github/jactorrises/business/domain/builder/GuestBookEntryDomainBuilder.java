package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.GuestBookEntryDomain;
import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.client.GuestBookEntryEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public class GuestBookEntryDomainBuilder extends DomainBuilder<GuestBookEntryDomain> {
    static final String THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK = "The entry must belong to a guest book";
    static final String THE_ENTRY_CANNOT_BE_EMPTY = "The entry cannot be empty";
    private static final String THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE = "The entry must be created by someone";

    private final GuestBookEntryEntity guestBookEntryEntity = newInstanceOf(GuestBookEntryEntity.class);

    private GuestBookEntryDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getEntry()) ? Optional.empty() : Optional.of(THE_ENTRY_CANNOT_BE_EMPTY),
                domain -> domain.getGuestBook() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK),
                domain -> domain.getCreatorName() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE)
        ));
    }

    public GuestBookEntryDomainBuilder withEntryAs(String entry, String guestName) {
        guestBookEntryEntity.setEntry(entry);
        guestBookEntryEntity.setCreatorName(guestName);
        return this;
    }

    public GuestBookEntryDomainBuilder with(GuestBookEntity guestBookEntity) {
        guestBookEntryEntity.setGuestBook(guestBookEntity);
        return this;
    }

    @Override protected GuestBookEntryDomain addhRequiredFields() {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }

    public static GuestBookEntryDomainBuilder init() {
        return new GuestBookEntryDomainBuilder();
    }
}
