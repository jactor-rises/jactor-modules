package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.client.domain.Persistent;

public interface GuestBookEntryEntity extends GuestBookEntry, Persistent<Long> {
    @Override GuestBookEntity getGuestBook();

    void setGuestBook(GuestBookEntity guestBookEntity);

    void setEntry(String entry);

    void setCreatorName(String creatorName);
}
