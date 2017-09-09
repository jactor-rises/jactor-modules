package com.github.jactorrises.model.internal.domain;

import com.github.jactorrises.model.internal.domain.builder.BlogDomainBuilder;
import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.internal.persistence.entity.blog.BlogEntity;

import java.time.LocalDate;

public class BlogDomain extends PersistentDomain<BlogEntity, Long> implements Blog {

    public BlogDomain(BlogEntity blogEntity) {
        super(blogEntity);
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public User getUser() {
        return getEntity().getUser();
    }

    @Override
    public LocalDate getCreated() {
        return getEntity().getCreated();
    }

    public static BlogDomainBuilder aBlog() {
        return new BlogDomainBuilder();
    }
}
