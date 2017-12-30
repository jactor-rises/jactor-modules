package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.DomainValidator;
import com.github.jactorrises.persistence.builder.GuestBookEntityBuilder;
import com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.GuestBookEntryEntity;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder.aGuestBookEntry;

public final class GuestBookEntryBuilder extends AbstractBuilder<GuestBookEntryDomain> {
    private final GuestBookEntryEntityBuilder guestBookEntryEntityBuilder = aGuestBookEntry();

    GuestBookEntryBuilder() {
        super(configureValidator());
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

    @Override protected GuestBookEntryDomain buildDomain() {
        return new GuestBookEntryDomain(guestBookEntryEntityBuilder.build());
    }

    private static DomainValidator<GuestBookEntryDomain> configureValidator() {
        return new DomainValidator<GuestBookEntryDomain>() {

            @Override public void validate(GuestBookEntryDomain domain) {
                addIfInvalid(StringUtils.isBlank(domain.getEntry()), "entry", FieldValidation.EMPTY);
                addIfInvalid(domain.getGuestBook() == null, "guestBook", FieldValidation.REQUIRED);
                addIfInvalid(domain.getCreatorName() == null, "creatorName", FieldValidation.REQUIRED);
            }
        };
    }

    public static GuestBookEntryDomain build(GuestBookEntryEntity guestBookEntryEntity) {
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }
}
