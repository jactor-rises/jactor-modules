package com.github.jactorrises.business.domain;

import com.github.jactorrises.business.domain.builder.GuestBookEntryDomainBuilder;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.persistence.client.GuestBookEntryEntity;

import java.time.LocalDateTime;

public class GuestBookEntryDomain extends PersistentDomain<GuestBookEntryEntity, Long> implements GuestBookEntry {

    public GuestBookEntryDomain(GuestBookEntryEntity guestBookEntryEntity) {
        super(guestBookEntryEntity);
    }

    @Override
    public GuestBook getGuestBook() {
        return getEntity().getGuestBook();
    }

    @Override public LocalDateTime getCreatedTime() {
        return getEntity().getCreatedTime();
    }

    @Override
    public String getEntry() {
        return getEntity().getEntry();
    }

    @Override public Name getCreatorName() {
        return getEntity().getCreatorName();
    }

    public static GuestBookEntryDomainBuilder aGuestBookEntry() {
        return GuestBookEntryDomainBuilder.init();
    }
}
