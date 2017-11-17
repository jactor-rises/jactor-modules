package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.Blog;

public interface BlogEntity extends Blog {
    void setTitle(String title);

    void setUserEntity(UserEntity userEntity);
}
