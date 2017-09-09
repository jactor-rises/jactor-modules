package com.github.jactorrises.model.internal.domain.blog;

import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.model.internal.persistence.entity.blog.BlogEntryEntity;

import java.time.LocalDateTime;

public class BlogEntryDomain extends PersistentDomain<BlogEntryEntity, Long> implements BlogEntry {

    public BlogEntryDomain(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
    }

    @Override
    public Blog getBlog() {
        return getEntity().getBlog();
    }

    @Override public LocalDateTime getCreatedTime() {
        return getEntity().getCreatedTime();
    }

    @Override
    public String getEntry() {
        return getEntity().getEntry();
    }

    @Override public Name getCreatorName() {
        return getEntity().getCreatorName();
    }

    public static BlogEntryDomainBuilder aBlogEntry() {
        return BlogEntryDomainBuilder.init();
    }
}
