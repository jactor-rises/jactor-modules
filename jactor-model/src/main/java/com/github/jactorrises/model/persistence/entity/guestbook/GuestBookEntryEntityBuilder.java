package com.github.jactorrises.model.persistence.entity.guestbook;

public class GuestBookEntryEntityBuilder {
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

    public GuestBookEntryEntity build() {
        GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity();
        guestBookEntryEntity.setCreatorName(creatorName);
        guestBookEntryEntity.setEntry(entry);
        guestBookEntryEntity.setGuestBook(guestBookEntity);

        return guestBookEntryEntity;
    }
}
