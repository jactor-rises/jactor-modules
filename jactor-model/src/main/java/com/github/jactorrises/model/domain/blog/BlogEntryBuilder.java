package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.ValidInstance;
import com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;
import static com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder.aBlogEntry;

public final class BlogEntryBuilder extends AbstractBuilder<BlogEntryDomain> {
    private final BlogEntryEntityBuilder blogEntryEntityBuilder = aBlogEntry();

    BlogEntryBuilder() {
        super(BlogEntryBuilder::validateInstance);
    }

    BlogEntryBuilder withEntry(String entry) {
        blogEntryEntityBuilder.withEntry(entry);
        return this;
    }


    BlogEntryBuilder withCreatorName(String creator) {
        blogEntryEntityBuilder.withCreatorName(creator);
        return this;
    }

    public BlogEntryBuilder with(BlogEntity blogEntity) {
        blogEntryEntityBuilder.with(blogEntity);
        return this;
    }

    public BlogEntryBuilder with(BlogEntityBuilder blogEntityBuilder) {
        return with(blogEntityBuilder.build());
    }

    private static Optional<String> validateInstance(BlogEntryDomain blogEntryDomain) {
        return ValidInstance.collectMessages(
                fetchMessageIfStringWithoutValue("entry", blogEntryDomain.getEntry()),
                fetchMessageIfFieldNotPresent("creatorName", blogEntryDomain.getCreatorName()),
                fetchMessageIfFieldNotPresent("blog", blogEntryDomain.getBlog())
        );
    }

    @Override protected BlogEntryDomain buildBean() {
        return new BlogEntryDomain(blogEntryEntityBuilder.build());
    }
}
