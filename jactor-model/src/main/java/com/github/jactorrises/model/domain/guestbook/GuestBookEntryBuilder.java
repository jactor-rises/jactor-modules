package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.persistence.dto.GuestBookDto;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;
import com.github.jactorrises.commons.builder.AbstractBuilder;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class GuestBookEntryBuilder extends AbstractBuilder<GuestBookEntryDomain> {
    private final GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();

    GuestBookEntryBuilder() {
        super(GuestBookEntryBuilder::validateInstance);
    }

    public GuestBookEntryBuilder withEntry(String entry, String guestName) {
        guestBookEntryDto.setCreatorName(new Name(guestName));
        guestBookEntryDto.setEntry(entry);

        return this;
    }

    public GuestBookEntryBuilder with(GuestBookDto guestBookDto) {
        guestBookEntryDto.setGuestBook(guestBookDto);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookBuilder guestBookBuilder) {
        return with(guestBookBuilder.build().getDto());
    }

    public GuestBookEntryBuilder with(GuestBookDomain guestBookDomain) {
        return with(guestBookDomain.getDto());
    }

    @Override protected GuestBookEntryDomain buildBean() {
        return new GuestBookEntryDomain(guestBookEntryDto);
    }

    private static Optional<String> validateInstance(GuestBookEntryDomain guestBookEntryDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("entry", guestBookEntryDomain.getEntry()),
                fetchMessageIfFieldNotPresent("guestBook", guestBookEntryDomain.getGuestBook()),
                fetchMessageIfFieldNotPresent("creatorName", guestBookEntryDomain.getCreatorName())
        );
    }

    public static GuestBookEntryDomain build(GuestBookEntryDto guestBookEntryDto) {
        return new GuestBookEntryDomain(guestBookEntryDto);
    }
}
