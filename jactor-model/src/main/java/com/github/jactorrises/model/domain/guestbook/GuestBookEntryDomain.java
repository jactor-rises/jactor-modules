package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;
import com.github.jactorrises.model.domain.PersistentDomain;

import java.time.LocalDateTime;

public class GuestBookEntryDomain extends PersistentDomain implements GuestBookEntry {

    private final GuestBookEntryDto guestBookEntryDto;

    public GuestBookEntryDomain(GuestBookEntryDto guestBookEntryDto) {
        this.guestBookEntryDto = guestBookEntryDto;
    }

    @Override public GuestBookDomain getGuestBook() {
        return guestBookEntryDto.getGuestBook() != null ? new GuestBookDomain(guestBookEntryDto.getGuestBook()) : null;
    }

    @Override public LocalDateTime getCreatedTime() {
        return guestBookEntryDto.getCreatedTime();
    }

    @Override public String getEntry() {
        return guestBookEntryDto.getEntry();
    }

    @Override public Name getCreatorName() {
        return new Name(guestBookEntryDto.getCreatorName());
    }

    @Override public GuestBookEntryDto getDto() {
        return guestBookEntryDto;
    }

    public static GuestBookEntryBuilder aGuestBookEntry() {
        return new GuestBookEntryBuilder();
    }
}
