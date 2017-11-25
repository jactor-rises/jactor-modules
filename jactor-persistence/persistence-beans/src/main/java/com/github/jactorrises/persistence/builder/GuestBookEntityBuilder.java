package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookOrm;
import com.github.jactorrises.persistence.entity.user.UserOrm;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;

public class GuestBookEntityBuilder {

    private String title;
    private UserOrm userEntity;

    private GuestBookEntityBuilder() {
    }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
        this.userEntity = (UserOrm) userEntity;
        return this;
    }

    public GuestBookEntity build() {
        GuestBookOrm guestBookOrm = new GuestBookOrm();
        guestBookOrm.setTitle(title);
        guestBookOrm.setUser(userEntity);

        return guestBookOrm;
    }

    public static GuestBookEntityBuilder aGuestBook() {
        return new GuestBookEntityBuilder();
    }
}
