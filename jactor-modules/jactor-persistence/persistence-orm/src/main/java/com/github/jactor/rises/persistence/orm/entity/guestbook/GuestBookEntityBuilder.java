package com.github.jactor.rises.persistence.orm.entity.guestbook;

import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.persistence.orm.entity.user.UserEntity;

import java.util.Optional;

public class GuestBookEntityBuilder extends AbstractBuilder<GuestBookEntity> {

    private String title;
    private UserEntity userEntity;

    GuestBookEntityBuilder() {
        super((gb, be) -> Optional.empty());
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
