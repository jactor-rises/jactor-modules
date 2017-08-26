package nu.hjemme.business.domain;

import nu.hjemme.business.domain.builder.BlogEntryDomainBuilder;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.Blog;
import nu.hjemme.client.domain.BlogEntry;
import nu.hjemme.persistence.client.BlogEntryEntity;

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
