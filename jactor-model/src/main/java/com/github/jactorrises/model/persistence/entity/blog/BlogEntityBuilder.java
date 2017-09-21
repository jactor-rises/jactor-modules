package com.github.jactorrises.model.persistence.entity.blog;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

public class BlogEntityBuilder extends Builder<BlogEntity> {
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

    @Override protected BlogEntity buildBean() {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(title);
        blogEntity.setUserEntity(userEntity);

        return blogEntity;
    }
}
