package com.github.jactor.rises.model.domain.guestbook;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.client.dto.GuestBookEntryDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.commons.builder.MissingFields;

import java.util.Optional;

public final class GuestBookEntryBuilder extends AbstractBuilder<GuestBookEntryDomain> {
    private final GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();

    GuestBookEntryBuilder() {
        super(GuestBookEntryBuilder::validateInstance);
    }

    public GuestBookEntryBuilder withEntry(String entry, String creatorName) {
        guestBookEntryDto.setCreatorName(creatorName);
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

    private static Optional<MissingFields> validateInstance(GuestBookEntryDomain guestBookEntryDomain, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenBlank("entry", guestBookEntryDomain.getEntry());
        missingFields.addInvalidFieldWhenNoValue("guestBook", guestBookEntryDomain.getGuestBook());
        missingFields.addInvalidFieldWhenNoValue("createdBy", guestBookEntryDomain.getCreatedBy());

        return missingFields.presentWhenFieldsAreMissing();
    }

    public static GuestBookEntryDomain build(GuestBookEntryDto guestBookEntryDto) {
        return new GuestBookEntryDomain(guestBookEntryDto);
    }
}
