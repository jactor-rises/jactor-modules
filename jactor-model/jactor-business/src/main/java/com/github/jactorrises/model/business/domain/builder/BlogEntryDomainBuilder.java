package com.github.jactorrises.model.business.domain.builder;

import com.github.jactorrises.model.business.domain.BlogEntryDomain;
import com.github.jactorrises.model.business.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.business.persistence.entity.blog.BlogEntryEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public class BlogEntryDomainBuilder extends DomainBuilder<BlogEntryDomain> {
    static final String THE_ENTRY_MUST_BELONG_TO_A_BLOG = "The blog entry must belong to a blog";
    static final String THE_ENTRY_CANNOT_BE_EMPTY = "The entry field cannot be empty";
    static final String THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE = "The entry must be created by someone";

    private final BlogEntryEntity blogEntryEntity = new BlogEntryEntity();

    private BlogEntryDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getEntry()) ? Optional.empty() : Optional.of(THE_ENTRY_CANNOT_BE_EMPTY),
                domain -> domain.getCreatorName() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE),
                domain -> domain.getBlog() != null ? Optional.empty() : Optional.of(THE_ENTRY_MUST_BELONG_TO_A_BLOG)
        ));
    }

    public BlogEntryDomainBuilder withEntryAs(String entry, String creator) {
        blogEntryEntity.setEntry(entry);
        blogEntryEntity.setCreatorName(creator);
        return this;
    }

    public BlogEntryDomainBuilder with(BlogEntity blogEntity) {
        blogEntryEntity.setBlog(blogEntity);
        return this;
    }

    @Override protected BlogEntryDomain addhRequiredFields() {
        return new BlogEntryDomain(blogEntryEntity);
    }

    public static BlogEntryDomainBuilder init() {
        return new BlogEntryDomainBuilder();
    }
}
