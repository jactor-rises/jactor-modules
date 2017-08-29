package com.github.jactorrises.persistence.boot.entity.blog;

import com.github.jactorrises.persistence.client.UserEntity;

public class BlogEntityBuilder {
    private String title;
    private UserEntity userEntity;

    BlogEntityBuilder() {
    }

    public BlogEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogEntityBuilder with(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public BlogEntityImpl build() {
        BlogEntityImpl blogEntity = new BlogEntityImpl();
        blogEntity.setTitle(title);
        blogEntity.setUserEntity(userEntity);

        return blogEntity;
    }
}
