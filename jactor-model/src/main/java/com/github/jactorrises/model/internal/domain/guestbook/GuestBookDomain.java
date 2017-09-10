package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.model.internal.domain.user.UserDomainBuilder;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;

public class GuestBookDomain extends PersistentDomain<GuestBookEntity, Long> implements GuestBook {

    GuestBookDomain(GuestBookEntity guestBookEntity) {
        super(guestBookEntity);
    }

    @Override public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public UserDomain getUser() {
        return userEntity() != null ? UserDomainBuilder.build(userEntity()) : null;
    }

    private UserEntity userEntity() {
        return getEntity().getUser();
    }

    static GuestBookDomainBuilder aGuestBook() {
        return new GuestBookDomainBuilder();
    }
}
