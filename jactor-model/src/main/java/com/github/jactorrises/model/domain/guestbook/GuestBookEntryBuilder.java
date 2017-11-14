package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.domain.DomainBuilder;
import com.github.jactorrises.model.domain.DomainValidator;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntityBuilder;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;

public final class GuestBookEntryBuilder extends DomainBuilder<GuestBookEntryDomain> {
    private final GuestBookEntryEntityBuilder guestBookEntryEntityBuilder = aGuestBookEntry();

    GuestBookEntryBuilder() {
        super(configureValidator());
    }

    public GuestBookEntryBuilder withEntry(String entry, String guestName) {
        guestBookEntryEntityBuilder
                .withEntry(entry)
                .withCreatorName(guestName);

        return this;
    }

    public GuestBookEntryBuilder with(GuestBookEntity guestBookEntity) {
        guestBookEntryEntityBuilder.with(guestBookEntity);
        return this;
    }

    public GuestBookEntryBuilder with(GuestBookDomain guestBookDomain) {
        return with(guestBookDomain.getEntity());
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
