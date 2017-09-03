package com.github.jactorrises.persistence.client;

import com.github.jactorrises.client.domain.BlogEntry;

public interface BlogEntryEntity extends BlogEntry, PersistentEntry {

    @Override BlogEntity getBlog();

    void setBlog(BlogEntity blog);
}
