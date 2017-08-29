package com.github.jactorrises.persistence.boot.entity.guestbook;

import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.client.UserEntity;

public class GuestBookEntityBuilder {

    private String title;
    private UserEntity userEntity;

    GuestBookEntityBuilder() { }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public GuestBookEntity build() {
        GuestBookEntityImpl guestBookEntity = new GuestBookEntityImpl();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }
}
