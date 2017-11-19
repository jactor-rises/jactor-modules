package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.user.UserOrm;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;

public class BlogEntityBuilder {
    private String title;
    private UserOrm userOrm;

    private BlogEntityBuilder() {
    }

    public BlogEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogEntityBuilder with(UserEntity useruserEntity) {
        this.userOrm = (UserOrm) useruserEntity;
        return this;
    }

    public BlogEntity build() {
        BlogOrm blogOrm = new BlogOrm();
        blogOrm.setTitle(title);
        blogOrm.setUserEntity(userOrm);

        return blogOrm;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
