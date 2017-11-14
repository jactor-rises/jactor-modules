package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntryEntity;

import java.time.LocalDateTime;

public class BlogEntryDomain extends PersistentDomain<BlogEntryEntity, Long> implements BlogEntry {

    private final BlogEntryEntity blogEntryEntity;

    BlogEntryDomain(BlogEntryEntity blogEntryEntity) {
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

    @Override public BlogEntryEntity getEntity() {
        return blogEntryEntity;
    }

    static BlogEntryBuilder aBlogEntry() {
        return new BlogEntryBuilder();
    }
}
