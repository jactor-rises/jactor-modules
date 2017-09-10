package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntryEntity;

import java.time.LocalDateTime;

public class GuestBookEntryDomain extends PersistentDomain<GuestBookEntryEntity, Long> implements GuestBookEntry {

    GuestBookEntryDomain(GuestBookEntryEntity guestBookEntryEntity) {
        super(guestBookEntryEntity);
    }

    @Override public GuestBookDomain getGuestBook() {
        return guestBookEntity() != null ? GuestBookDomainBuilder.build(guestBookEntity()) : null;
    }

    private GuestBookEntity guestBookEntity() {
        return getEntity().getGuestBook();
    }

    @Override public LocalDateTime getCreatedTime() {
        return getEntity().getCreatedTime();
    }

    @Override public String getEntry() {
        return getEntity().getEntry();
    }

    @Override public Name getCreatorName() {
        return getEntity().getCreatorName();
    }

    static GuestBookEntryDomainBuilder aGuestBookEntry() {
        return new GuestBookEntryDomainBuilder();
    }
}
