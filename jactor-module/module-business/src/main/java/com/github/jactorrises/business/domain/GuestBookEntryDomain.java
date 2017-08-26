package com.github.jactorrises.business.domain;

import com.github.jactorrises.business.domain.builder.GuestBookEntryDomainBuilder;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.GuestBook;
import nu.hjemme.client.domain.GuestBookEntry;
import nu.hjemme.persistence.client.GuestBookEntryEntity;

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
