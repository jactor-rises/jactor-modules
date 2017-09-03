package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.user.UserEntity;

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

    public BlogEntity build() {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(title);
        blogEntity.setUserEntity(userEntity);

        return blogEntity;
    }
}
