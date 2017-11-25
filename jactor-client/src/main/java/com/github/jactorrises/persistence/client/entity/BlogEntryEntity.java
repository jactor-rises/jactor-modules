package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.client.domain.Persistent;

public interface BlogEntryEntity extends BlogEntry, Persistent<Long> {
    void setBlog(BlogEntity blog);

    void setCreatorName(String creator);

    void setEntry(String entry);
}
