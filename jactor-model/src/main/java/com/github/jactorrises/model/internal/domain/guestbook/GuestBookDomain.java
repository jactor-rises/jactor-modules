package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;

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
