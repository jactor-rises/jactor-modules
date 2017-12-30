package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.DomainValidator;
import com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder;
import com.github.jactorrises.persistence.builder.UserEntityBuilder;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder.aBlog;

public final class BlogBuilder extends AbstractBuilder<BlogDomain> {
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

    @Override protected BlogDomain buildDomain() {
        return new BlogDomain(blogEntityBuilder.build());
    }

    private static DomainValidator<BlogDomain> configureValidator() {
        return new DomainValidator<BlogDomain>() {

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
