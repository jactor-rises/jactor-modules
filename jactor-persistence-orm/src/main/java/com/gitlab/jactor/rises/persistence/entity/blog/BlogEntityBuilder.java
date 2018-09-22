package com.gitlab.jactor.rises.persistence.entity.blog;

import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntityBuilder;

import java.util.Optional;

public class BlogEntityBuilder extends AbstractBuilder<BlogEntity> {
    private String title;
    private UserEntity userEntity;

    BlogEntityBuilder() {
        super(BlogEntityBuilder::validate);
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

    private static Optional<MissingFields> validate(BlogEntity blogEntity, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenBlank("title", blogEntity.getTitle());
        missingFields.addInvalidFieldWhenNoValue("userEntity", blogEntity.getUser());
        return missingFields.presentWhenFieldsAreMissing();
    }
}
