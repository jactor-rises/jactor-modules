package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.builder.GuestBookEntityBuilder;
import com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.GuestBookEntryEntity;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;
import static com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder.aGuestBookEntry;

public final class GuestBookEntryBuilder extends AbstractBuilder<GuestBookEntryDomain> {
    private final GuestBookEntryEntityBuilder guestBookEntryEntityBuilder = aGuestBookEntry();

    GuestBookEntryBuilder() {
        super(GuestBookEntryBuilder::validateInstance);
    }

    public GuestBookEntryBuilder withEntry(String entry, String guestName) {
        guestBookEntryEntityBuilder
                .withCreatorName(guestName)
                .withEntry(entry);

        return this;
    }

    public GuestBookEntryBuilder with(GuestBookEntity guestBookEntity) {
        guestBookEntryEntityBuilder.with(guestBookEntity);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookDomain guestBookDomain) {
        return with(guestBookDomain.getPersistence());
    }

    public GuestBookEntryBuilder with(GuestBookEntityBuilder guestBookEntityBuilder) {
        return with(guestBookEntityBuilder.build());
    }

    @Override protected GuestBookEntryDomain buildBean() {
        return new GuestBookEntryDomain(guestBookEntryEntityBuilder.build());
    }

    private static Optional<String> validateInstance(GuestBookEntryDomain guestBookEntryDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("entry", guestBookEntryDomain.getEntry()),
                fetchMessageIfFieldNotPresent("guestBook", guestBookEntryDomain.getGuestBook()),
                fetchMessageIfFieldNotPresent("creatorName", guestBookEntryDomain.getCreatorName())
        );
    }

    public static GuestBookEntryDomain build(GuestBookEntryEntity guestBookEntryEntity) {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }
}
