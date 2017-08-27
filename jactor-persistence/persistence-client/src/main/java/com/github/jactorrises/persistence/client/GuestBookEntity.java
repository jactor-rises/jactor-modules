package com.github.jactorrises.persistence.client;

import com.github.jactorrises.client.domain.GuestBook;

public interface GuestBookEntity extends GuestBook {
    void setTitle(String title);

    void setUser(UserEntity userEntity);
}
