package com.gitlab.jactor.rises.model.domain.guestbook;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.model.domain.GuestBookEntry;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.model.domain.PersistentDomain;

import java.time.LocalDateTime;
import java.util.Optional;

public class GuestBookEntryDomain extends PersistentDomain implements GuestBookEntry {

    private final GuestBookEntryDto guestBookEntryDto;

    public GuestBookEntryDomain(GuestBookEntryDto guestBookEntryDto) {
        this.guestBookEntryDto = guestBookEntryDto;
    }

    @Override public GuestBookDomain getGuestBook() {
        return Optional.ofNullable(guestBookEntryDto.getGuestBook()).map(GuestBookDomain::new).orElse(null);
    }

    @Override public LocalDateTime getCreatedTime() {
        return guestBookEntryDto.getCreationTime();
    }

    @Override public String getEntry() {
        return guestBookEntryDto.getEntry();
    }

    @Override public Name getCreatorName() {
        return Optional.ofNullable(guestBookEntryDto.getCreatorName()).map(Name::new).orElse(null);
    }

    @Override public GuestBookEntryDto getDto() {
        return guestBookEntryDto;
    }

    public static GuestBookEntryBuilder aGuestBookEntry() {
        return new GuestBookEntryBuilder();
    }
}
