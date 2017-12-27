package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.guestbook.GuestBookOrm;
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

    public GuestBookOrm build() {
        GuestBookOrm guestBookOrm = new GuestBookOrm();
        guestBookOrm.setTitle(title);
        guestBookOrm.setUser(userEntity);

        return guestBookOrm;
    }

    public static GuestBookEntityBuilder aGuestBook() {
        return new GuestBookEntityBuilder();
    }
}
