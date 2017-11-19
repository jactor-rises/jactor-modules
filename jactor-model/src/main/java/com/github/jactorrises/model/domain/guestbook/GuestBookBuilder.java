package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.domain.DomainBuilder;
import com.github.jactorrises.model.domain.DomainValidator;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntityBuilder;
import com.github.jactorrises.persistence.entity.user.UserEntityBuilder;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.persistence.entity.guestbook.GuestBookEntityBuilder.aGuestBook;

public final class GuestBookBuilder extends DomainBuilder<GuestBookDomain> {
    private final GuestBookEntityBuilder guestBookEntity = aGuestBook();

    GuestBookBuilder() {
        super(configureValidatetor());
    }

    public GuestBookBuilder withTitle(String title) {
        guestBookEntity.withTitle(title);
        return this;
    }

    public GuestBookBuilder with(UserEntity userEntity) {
        guestBookEntity.with(userEntity);
        return this;
    }

    public GuestBookBuilder with(UserEntityBuilder userEntityBuilder) {
        return with(userEntityBuilder.build());
    }

    public GuestBookBuilder with(UserDomain userDomain) {
        with(userDomain.getEntity());
        return this;
    }

    @Override protected GuestBookDomain buildDomain() {
        return new GuestBookDomain(guestBookEntity.build());
    }

    private static DomainValidator<GuestBookDomain> configureValidatetor() {
        return new DomainValidator<GuestBookDomain>() {

            @Override public void validate(GuestBookDomain domain) {
                addIfInvalid(StringUtils.isBlank(domain.getTitle()), "title", FieldValidation.EMPTY);
                addIfInvalid(domain.getUser() == null, "user", FieldValidation.REQUIRED);
            }
        };
    }

    public static GuestBookDomain build(GuestBookEntity guestBook) {
        return new GuestBookDomain(guestBook);
    }
}
