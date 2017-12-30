package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.builder.GuestBookEntityBuilder;
import com.github.jactorrises.persistence.builder.UserEntityBuilder;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;
import static com.github.jactorrises.persistence.builder.GuestBookEntityBuilder.aGuestBook;

public final class GuestBookBuilder extends AbstractBuilder<GuestBookDomain> {
    private final GuestBookEntityBuilder guestBookEntity = aGuestBook();

    GuestBookBuilder() {
        super(GuestBookBuilder::validateInstance);
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
        with(userDomain.getPersistence());
        return this;
    }

    @Override protected GuestBookDomain buildBean() {
        return new GuestBookDomain(guestBookEntity.build());
    }

    private static Optional<String> validateInstance(GuestBookDomain guestBookDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("title", guestBookDomain.getTitle()),
                fetchMessageIfFieldNotPresent("user", guestBookDomain.getUser())
        );
    }

    public static GuestBookDomain build(GuestBookEntity guestBook) {
        return new GuestBookDomain(guestBook);
    }
}
