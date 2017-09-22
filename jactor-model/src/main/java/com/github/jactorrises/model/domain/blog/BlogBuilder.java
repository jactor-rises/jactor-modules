package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntityBuilder;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.blog.BlogEntity.aBlog;
import static java.util.Arrays.asList;

public final class BlogBuilder extends Builder<BlogDomain> {
    static final String THE_BLOG_MUST_BELONG_TO_A_USER = "The blog must belong to a user";
    static final String THE_BLOG_MUST_HAVE_A_TITLE = "The blog must have a title";

    private final BlogEntityBuilder blogEntityBuilder = aBlog();

    BlogBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_BLOG_MUST_HAVE_A_TITLE),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_BLOG_MUST_BELONG_TO_A_USER)
        ));
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

    public static BlogDomain build(BlogEntity blogEntity) {
        return new BlogDomain(blogEntity);
    }
}
