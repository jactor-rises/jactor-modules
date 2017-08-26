package nu.hjemme.persistence.client;

import nu.hjemme.client.domain.BlogEntry;

public interface BlogEntryEntity extends BlogEntry, PersistentEntry {

    @Override BlogEntity getBlog();

    void setBlog(BlogEntity blog);
}
