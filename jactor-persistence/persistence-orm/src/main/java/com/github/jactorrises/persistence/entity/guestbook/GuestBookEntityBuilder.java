package com.github.jactorrises.persistence.entity.guestbook;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.entity.user.UserEntity;

public class GuestBookEntityBuilder extends AbstractBuilder<GuestBookEntity> {

    private String title;
    private UserEntity userEntity;

    GuestBookEntityBuilder() {
    }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    @Override protected GuestBookEntity buildBean() {
        GuestBookEntity guestBookEntity = new GuestBookEntity();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }
}
