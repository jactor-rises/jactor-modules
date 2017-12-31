package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.dto.GuestBookDto;

public class GuestBookDomain extends PersistentDomain<Long> implements GuestBook {

    private final GuestBookDto guestBookDto;

    public GuestBookDomain(GuestBookDto guestBookDto) {
        this.guestBookDto = guestBookDto;
    }

    @Override public String getTitle() {
        return guestBookDto.getTitle();
    }

    @Override public UserDomain getUser() {
        return guestBookDto.getUser() != null ? new UserDomain(guestBookDto.getUser()) : null;
    }

    @Override public GuestBookDto getPersistence() {
        return guestBookDto;
    }

    public static GuestBookBuilder aGuestBook() {
        return new GuestBookBuilder();
    }
}
