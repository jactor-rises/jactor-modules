package com.github.jactor.rises.model.domain.guestbook;

import com.github.jactor.rises.client.domain.GuestBook;
import com.github.jactor.rises.client.domain.GuestBookEntry;
import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.util.Set;
import java.util.stream.Collectors;

public class GuestBookDomain extends PersistentDomain implements GuestBook {

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

    @Override public Set<GuestBookEntry> getEntries() {
        return guestBookDto.getEntries().stream().map(GuestBookEntryDomain::new).collect(Collectors.toSet());
    }

    @Override public GuestBookDto getDto() {
        return guestBookDto;
    }

    public static GuestBookBuilder aGuestBook() {
        return new GuestBookBuilder();
    }
}
