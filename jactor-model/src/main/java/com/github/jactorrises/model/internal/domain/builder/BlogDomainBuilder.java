package com.github.jactorrises.model.internal.domain.builder;

import com.github.jactorrises.model.internal.domain.BlogDomain;
import com.github.jactorrises.model.internal.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public class BlogDomainBuilder extends DomainBuilder<BlogDomain> {
    static final String THE_BLOG_MUST_BELONG_TO_A_USER = "The blog must belong to a user";
    static final String THE_BLOG_MUST_HAVE_A_TITLE = "The blog must have a title";

    private final BlogEntity blogEntity = new BlogEntity();

    public BlogDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_BLOG_MUST_HAVE_A_TITLE),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_BLOG_MUST_BELONG_TO_A_USER)
        ));
    }

    public BlogDomainBuilder withTitleAs(String title) {
        blogEntity.setTitle(title);
        return this;
    }

    public BlogDomainBuilder with(UserEntity userEntity) {
        blogEntity.setUserEntity(userEntity);
        return this;
    }

    @Override protected BlogDomain addRequiredFields() {
        return new BlogDomain(blogEntity);
    }
}
