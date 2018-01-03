package com.github.jactorrises.persistence.orm.entity.guestbook;

import com.github.jactorrises.commons.builder.AbstractBuilder;

public class GuestBookEntryEntityBuilder extends AbstractBuilder<GuestBookEntryEntity> {
    private GuestBookEntity guestBookEntity;
    private String creatorName;
    private String entry;

    GuestBookEntryEntityBuilder() {
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

    @Override protected GuestBookEntryEntity buildBean() {
        GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity();
        guestBookEntryEntity.setCreatorName(creatorName);
        guestBookEntryEntity.setEntry(entry);
        guestBookEntryEntity.setGuestBook(guestBookEntity);

        return guestBookEntryEntity;
    }
}
