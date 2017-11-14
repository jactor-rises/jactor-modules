package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.DomainValidater;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntityBuilder;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity.aGuestBook;

public final class GuestBookBuilder extends Builder<GuestBookDomain> {
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

    @Override protected GuestBookDomain buildBean() {
        return new GuestBookDomain(guestBookEntity.build());
    }

    private static DomainValidater<GuestBookDomain> configureValidatetor() {
        return new DomainValidater<GuestBookDomain>() {

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
