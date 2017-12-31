package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.entity.user.UserOrm;

public class GuestBookEntityBuilder {

    private String title;
    private UserOrm userEntity;

    private GuestBookEntityBuilder() {
    }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserOrm userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public GuestBookEntity build() {
        GuestBookEntity guestBookEntity = new GuestBookEntity();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }

    public static GuestBookEntityBuilder aGuestBook() {
        return new GuestBookEntityBuilder();
    }
}
