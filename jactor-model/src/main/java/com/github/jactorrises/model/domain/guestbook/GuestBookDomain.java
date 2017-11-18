package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;

public class GuestBookDomain extends PersistentDomain<GuestBookEntity, Long> implements GuestBook {

    private final GuestBookEntity guestBookEntity;

    GuestBookDomain(GuestBookEntity guestBookEntity) {
        this.guestBookEntity = guestBookEntity;
    }

    @Override public String getTitle() {
        return guestBookEntity.getTitle();
    }

    @Override public UserDomain getUser() {
        return guestBookEntity.getUser() != null ? new UserDomain(guestBookEntity.getUser()) : null;
    }

    @Override public GuestBookEntity getEntity() {
        return guestBookEntity;
    }

    public static GuestBookBuilder aGuestBook() {
        return new GuestBookBuilder();
    }
}
