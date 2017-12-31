package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.user.UserEntity;

public class BlogEntityBuilder {
    private String title;
    private UserEntity useruserEntity;

    private BlogEntityBuilder() {
    }

    public BlogEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogEntityBuilder with(UserEntity userEntity) {
        this.useruserEntity = userEntity;
        return this;
    }

    public BlogEntity build() {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(title);
        blogEntity.setUserEntity(useruserEntity);

        return blogEntity;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
