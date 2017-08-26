package nu.hjemme.business.domain;

import nu.hjemme.business.domain.builder.BlogDomainBuilder;
import nu.hjemme.client.domain.Blog;
import nu.hjemme.client.domain.User;
import nu.hjemme.persistence.client.BlogEntity;

import java.time.LocalDate;

public class BlogDomain extends PersistentDomain<BlogEntity, Long> implements Blog {

    public BlogDomain(BlogEntity blogEntity) {
        super(blogEntity);
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public User getUser() {
        return getEntity().getUser();
    }

    @Override
    public LocalDate getCreated() {
        return getEntity().getCreated();
    }

    public static BlogDomainBuilder aBlog() {
        return new BlogDomainBuilder();
    }
}
