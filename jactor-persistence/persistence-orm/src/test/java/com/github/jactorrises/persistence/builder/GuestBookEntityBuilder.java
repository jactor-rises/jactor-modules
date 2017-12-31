package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.entity.user.UserEntity;

public class GuestBookEntityBuilder {

    private String title;
    private UserEntity userEntity;

    private GuestBookEntityBuilder() {
    }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
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
