package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.persistence.entity.blog.BlogEntryOrm;

import java.time.LocalDateTime;

public class BlogEntryDomain extends PersistentDomain<Long> implements BlogEntry {

    private final BlogEntryOrm blogEntryEntity;

    BlogEntryDomain(BlogEntryOrm blogEntryEntity) {
        this.blogEntryEntity = blogEntryEntity;
    }

    @Override public BlogDomain getBlog() {
        return blogEntryEntity.getBlog() != null ? new BlogDomain(blogEntryEntity.getBlog()) : null;
    }

    @Override public LocalDateTime getCreatedTime() {
        return blogEntryEntity.getCreatedTime();
    }

    @Override public String getEntry() {
        return blogEntryEntity.getEntry();
    }

    @Override public Name getCreatorName() {
        return blogEntryEntity.getCreatorName();
    }

    @Override public BlogEntryOrm getPersistence() {
        return blogEntryEntity;
    }

    static BlogEntryBuilder aBlogEntry() {
        return new BlogEntryBuilder();
    }
}
