package com.github.jactor.rises.model.domain.guestbook;

import com.github.jactor.rises.client.converter.FieldConverter;
import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.domain.GuestBookEntry;
import com.github.jactor.rises.client.dto.GuestBookEntryDto;
import com.github.jactor.rises.model.domain.PersistentDomain;

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
        return FieldConverter.convertDateTime(guestBookEntryDto.getCreatedTime());
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
