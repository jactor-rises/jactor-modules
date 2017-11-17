package com.github.jactorrises.model.persistence.entity.guestbook;

import com.github.jactorrises.model.persistence.entity.user.UserOrm;
import com.github.jactorrises.persistence.client.entity.UserEntity;

public class GuestBookEntityBuilder {

    private String title;
    private UserOrm userEntity;

    GuestBookEntityBuilder() { }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
        this.userEntity = (UserOrm) userEntity;
        return this;
    }

    public GuestBookEntity build() {
        GuestBookEntity guestBookEntity = new GuestBookEntity();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }
}
