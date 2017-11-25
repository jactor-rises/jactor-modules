package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.entity.BlogEntity;

import java.time.LocalDate;

public class BlogDomain extends PersistentDomain<Long> implements Blog {

    private final BlogEntity blogEntity;

    BlogDomain(BlogEntity blogEntity) {
        this.blogEntity = blogEntity;
    }

    @Override public String getTitle() {
        return blogEntity.getTitle();
    }

    @Override public UserDomain getUser() {
        return blogEntity.getUser() != null ? new UserDomain(blogEntity.getUser()) : null;
    }

    @Override public LocalDate getCreated() {
        return getPersistence().getCreated();
    }

    @Override public BlogEntity getPersistence() {
        return blogEntity;
    }

    static BlogBuilder aBlog() {
        return new BlogBuilder();
    }
}
