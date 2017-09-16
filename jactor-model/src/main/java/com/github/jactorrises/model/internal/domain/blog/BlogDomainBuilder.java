package com.github.jactorrises.model.internal.domain.blog;

import com.github.jactorrises.model.internal.domain.DomainBuilder;
import com.github.jactorrises.model.internal.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class BlogDomainBuilder extends DomainBuilder<BlogDomain> {
    static final String THE_BLOG_MUST_BELONG_TO_A_USER = "The blog must belong to a user";
    static final String THE_BLOG_MUST_HAVE_A_TITLE = "The blog must have a title";

    private final BlogEntity blogEntity = new BlogEntity();

    BlogDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_BLOG_MUST_HAVE_A_TITLE),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_BLOG_MUST_BELONG_TO_A_USER)
        ));
    }

    BlogDomainBuilder withTitleAs(String title) {
        blogEntity.setTitle(title);
        return this;
    }

    public BlogDomainBuilder with(UserEntity userEntity) {
        blogEntity.setUserEntity(userEntity);
        return this;
    }

    @Override protected BlogDomain buildBeforeValidation() {
        return new BlogDomain(blogEntity);
    }

    public static BlogDomain build(BlogEntity blogEntity) {
        return new BlogDomain(blogEntity);
    }
}