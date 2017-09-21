package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class GuestBookEntryBuilder extends Builder<GuestBookEntryDomain> {
    static final String THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK = "The entry must belong to a guest book";
    static final String THE_ENTRY_CANNOT_BE_EMPTY = "The entry cannot be empty";
    private static final String THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE = "The entry must be created by someone";

    private final GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity();

    GuestBookEntryBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getEntry()) ? Optional.empty() : Optional.of(THE_ENTRY_CANNOT_BE_EMPTY),
                domain -> domain.getGuestBook() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK),
                domain -> domain.getCreatorName() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE)
        ));
    }

    public GuestBookEntryBuilder withEntry(String entry, String guestName) {
        guestBookEntryEntity.setEntry(entry);
        guestBookEntryEntity.setCreatorName(guestName);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookEntity guestBookEntity) {
        guestBookEntryEntity.setGuestBook(guestBookEntity);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookDomain guestBookDomain) {
        return  with(guestBookDomain.getEntity());
    }

    @Override protected GuestBookEntryDomain buildBeforeValidation() {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }

    public static GuestBookEntryDomain build(GuestBookEntryEntity guestBookEntryEntity) {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }
}
