package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.client.domain.Persistent;

public interface GuestBookEntity extends GuestBook, Persistent<Long> {
    @Override UserEntity getUser();

    void setTitle(String title);

    void setUser(UserEntity user);
}
