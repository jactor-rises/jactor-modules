package com.github.jactor.rises.persistence.entity.blog;

import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntityBuilder;

public class BlogEntityBuilder extends AbstractBuilder<BlogEntity> {
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

    public BlogEntityBuilder with(UserEntityBuilder userEntityBuilder) {
        return with(userEntityBuilder.build());
    }

    @Override protected BlogEntity buildBean() {
        BlogEntity blogEntityToBuild = new BlogEntity();
        blogEntityToBuild.setTitle(title);
        blogEntityToBuild.setUserEntity(userEntity);

        return blogEntityToBuild;
    }
}
