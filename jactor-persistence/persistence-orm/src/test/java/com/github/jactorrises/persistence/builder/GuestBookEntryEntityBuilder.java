package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryEntity;

public class GuestBookEntryEntityBuilder {
    private GuestBookEntity guestBookEntity;
    private String creatorName;
    private String entry;

    private GuestBookEntryEntityBuilder() {
    }

    public GuestBookEntryEntityBuilder with(GuestBookEntity guestBookEntity) {
        this.guestBookEntity = guestBookEntity;
        return this;
    }

    public GuestBookEntryEntityBuilder withEntry(String entry) {
        this.entry = entry;
        return this;
    }

    public GuestBookEntryEntityBuilder withCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public GuestBookEntryEntity build() {
        GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity();
        guestBookEntryEntity.setCreatorName(creatorName);
        guestBookEntryEntity.setEntry(entry);
        guestBookEntryEntity.setGuestBook(guestBookEntity);

        return guestBookEntryEntity;
    }

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
