package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity;

import java.time.LocalDateTime;

public class GuestBookEntryDomain extends PersistentDomain<GuestBookEntryEntity, Long> implements GuestBookEntry {

    private final GuestBookEntryEntity guestBookEntryEntity;

    GuestBookEntryDomain(GuestBookEntryEntity guestBookEntryEntity) {
        this.guestBookEntryEntity = guestBookEntryEntity;
    }

    @Override public GuestBookDomain getGuestBook() {
        return guestBookEntryEntity.getGuestBook() != null ? new GuestBookDomain(guestBookEntryEntity.getGuestBook()) : null;
    }

    @Override public LocalDateTime getCreatedTime() {
        return guestBookEntryEntity.getCreatedTime();
    }

    @Override public String getEntry() {
        return guestBookEntryEntity.getEntry();
    }

    @Override public Name getCreatorName() {
        return guestBookEntryEntity.getCreatorName();
    }

    @Override public GuestBookEntryEntity getEntity() {
        return guestBookEntryEntity;
    }

    public static GuestBookEntryBuilder aGuestBookEntry() {
        return new GuestBookEntryBuilder();
    }
}
