package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.model.domain.user.UserDomainBuilder;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

import java.time.LocalDate;

public class BlogDomain extends PersistentDomain<BlogEntity, Long> implements Blog {

    BlogDomain(BlogEntity blogEntity) {
        super(blogEntity);
    }

    @Override public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public UserDomain getUser() {
        return userEntity() != null ? UserDomainBuilder.build(userEntity()) : null;
    }

    private UserEntity userEntity() {
        return getEntity().getUser();
    }

    @Override public LocalDate getCreated() {
        return getEntity().getCreated();
    }

    static BlogDomainBuilder aBlog() {
        return new BlogDomainBuilder();
    }
}
