package com.github.jactorrises.model.persistence.entity.blog;

import com.github.jactorrises.model.persistence.entity.user.UserEntity;

public class BlogEntityBuilder {
    private String title;
    private UserEntity userEntity;

    private BlogEntityBuilder() {
    }

    public BlogEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogEntityBuilder with(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public BlogOrm build() {
        BlogOrm blogOrm = new BlogOrm();
        blogOrm.setTitle(title);
        blogOrm.setUserEntity(userEntity);

        return blogOrm;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
