package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryOrm;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookOrm;

public class GuestBookEntryEntityBuilder {
    private GuestBookOrm guestBookEntity;
    private String creatorName;
    private String entry;

    private GuestBookEntryEntityBuilder() {
    }

    public GuestBookEntryEntityBuilder with(GuestBookOrm guestBookEntity) {
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

    public GuestBookEntryOrm build() {
        GuestBookEntryOrm guestBookEntryOrm = new GuestBookEntryOrm();
        guestBookEntryOrm.setCreatorName(creatorName);
        guestBookEntryOrm.setEntry(entry);
        guestBookEntryOrm.setGuestBook(guestBookEntity);

        return guestBookEntryOrm;
    }

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
