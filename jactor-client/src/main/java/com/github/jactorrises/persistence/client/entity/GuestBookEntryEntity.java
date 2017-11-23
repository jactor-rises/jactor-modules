package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.GuestBookEntry;

public interface GuestBookEntryEntity extends GuestBookEntry, PersistentEntity<Long> {
    @Override GuestBookEntity getGuestBook();

    void setGuestBook(GuestBookEntity guestBookEntity);

    void setEntry(String entry);

    void setCreatorName(String creatorName);
}
