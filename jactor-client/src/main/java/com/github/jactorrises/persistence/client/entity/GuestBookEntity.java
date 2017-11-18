package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.GuestBook;

public interface GuestBookEntity extends GuestBook {
    @Override UserEntity getUser();

    void setTitle(String title);

    void setUser(UserEntity user);
}
