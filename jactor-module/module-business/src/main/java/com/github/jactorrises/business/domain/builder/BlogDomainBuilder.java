package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.BlogDomain;
import nu.hjemme.persistence.client.BlogEntity;
import nu.hjemme.persistence.client.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public class BlogDomainBuilder extends DomainBuilder<BlogDomain> {
    static final String THE_BLOG_MUST_BELONG_TO_A_USER = "The blog must belong to a user";
    static final String THE_BLOG_MUST_HAVE_A_TITLE = "The blog must have a title";

    private final BlogEntity blogEntity = newInstanceOf(BlogEntity.class);

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

    @Override protected BlogDomain initWithRequiredFields() {
        return new BlogDomain(blogEntity);
    }
}
