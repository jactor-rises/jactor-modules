package com.github.jactorrises.business.domain;

import com.github.jactorrises.business.domain.builder.GuestBookDomainBuilder;
import nu.hjemme.client.domain.GuestBook;
import nu.hjemme.client.domain.User;
import nu.hjemme.persistence.client.GuestBookEntity;

public class GuestBookDomain extends PersistentDomain<GuestBookEntity, Long> implements GuestBook {

    public GuestBookDomain(GuestBookEntity guestBookEntity) {
        super(guestBookEntity);
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public User getUser() {
        return getEntity().getUser();
    }

    public static GuestBookDomainBuilder aGuestBook() {
        return GuestBookDomainBuilder.init();
    }
}
