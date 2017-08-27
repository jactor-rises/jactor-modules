package com.github.jactorrises.persistence.client;

import com.github.jactorrises.client.domain.GuestBookEntry;

public interface GuestBookEntryEntity extends GuestBookEntry, PersistentEntry {
    @Override GuestBookEntity getGuestBook();

    void setGuestBook(GuestBookEntity guestBook);
}
