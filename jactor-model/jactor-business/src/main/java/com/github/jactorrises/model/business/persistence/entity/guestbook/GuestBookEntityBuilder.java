package com.github.jactorrises.model.business.persistence.entity.guestbook;

import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;

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
        GuestBookEntity guestBookEntity = new GuestBookEntity();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }
}
