package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.BlogEntry;

public interface BlogEntryEntity extends BlogEntry, PersistentEntity<Long> {
    void setBlog(BlogEntity blog);

    void setCreatorName(String creator);

    void setEntry(String entry);
}
