package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.builder.UserEntityBuilder;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;
import static com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder.aBlog;

public final class BlogBuilder extends AbstractBuilder<BlogDomain> {
    private final BlogEntityBuilder blogEntityBuilder = aBlog();

    BlogBuilder() {
        super(BlogBuilder::validateInstance);
    }

    BlogBuilder withTitleAs(String title) {
        blogEntityBuilder.withTitle(title);
        return this;
    }

    public BlogBuilder with(UserEntity userEntity) {
        blogEntityBuilder.with(userEntity);
        return this;
    }

    public BlogBuilder with(UserEntityBuilder userEntityBuilder) {
        return with(userEntityBuilder.build());
    }

    @Override protected BlogDomain buildBean() {
        return new BlogDomain(blogEntityBuilder.build());
    }

    private static Optional<String> validateInstance(BlogDomain blogDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("title", blogDomain.getTitle()),
                fetchMessageIfFieldNotPresent("user", blogDomain.getUser())
        );
    }

    public static BlogDomain build(BlogEntity blogEntity) {
        return new BlogDomain(blogEntity);
    }
}
