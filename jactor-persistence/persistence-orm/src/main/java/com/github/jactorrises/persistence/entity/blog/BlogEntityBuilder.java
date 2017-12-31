package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.user.UserOrm;

public class BlogEntityBuilder {
    private String title;
    private UserOrm userOrm;

    private BlogEntityBuilder() {
    }

    public BlogEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogEntityBuilder with(UserOrm userEntity) {
        this.userOrm = userEntity;
        return this;
    }

    public BlogEntity build() {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(title);
        blogEntity.setUserEntity(userOrm);

        return blogEntity;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
