package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntryEntity;

import java.time.LocalDateTime;

public class BlogEntryDomain extends PersistentDomain<BlogEntryEntity, Long> implements BlogEntry {

    BlogEntryDomain(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
    }

    @Override public BlogDomain getBlog() {
        return blogEntity() != null ? BlogDomainBuilder.build(blogEntity()) : null;
    }

    private BlogEntity blogEntity() {
        return getEntity().getBlog();
    }

    @Override public LocalDateTime getCreatedTime() {
        return getEntity().getCreatedTime();
    }

    @Override public String getEntry() {
        return getEntity().getEntry();
    }

    @Override public Name getCreatorName() {
        return getEntity().getCreatorName();
    }

    static BlogEntryDomainBuilder aBlogEntry() {
        return new BlogEntryDomainBuilder();
    }
}
