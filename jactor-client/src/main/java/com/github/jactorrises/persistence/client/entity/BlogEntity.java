package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.client.domain.Persistent;

public interface BlogEntity extends Blog, Persistent<Long> {

    @Override UserEntity getUser();

    void setTitle(String title);

    void setUserEntity(UserEntity userEntity);
}
