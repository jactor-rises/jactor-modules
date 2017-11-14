package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.DomainValidater;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntityBuilder;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.model.persistence.entity.blog.BlogEntity.aBlog;

public final class BlogBuilder extends Builder<BlogDomain> {
    private final BlogEntityBuilder blogEntityBuilder = aBlog();

    BlogBuilder() {
        super(configureValidator());
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

    private static DomainValidater<BlogDomain> configureValidator() {
        return new DomainValidater<BlogDomain>() {

            @Override public void validate(BlogDomain domain) {
                addIfInvalid(StringUtils.isBlank(domain.getTitle()), "title", FieldValidation.EMPTY);
                addIfInvalid(domain.getUser() == null, "user", FieldValidation.REQUIRED);
            }
        };
    }

    public static BlogDomain build(BlogEntity blogEntity) {
        return new BlogDomain(blogEntity);
    }
}
