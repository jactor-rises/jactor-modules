package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.DomainValidator;
import com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder;
import com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import org.apache.commons.lang3.StringUtils;

import static com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder.aBlogEntry;

public final class BlogEntryBuilder extends AbstractBuilder<BlogEntryDomain> {
    private final BlogEntryEntityBuilder blogEntryEntityBuilder = aBlogEntry();

    BlogEntryBuilder() {
        super(configureValidatetor());
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

    private static DomainValidator<BlogEntryDomain> configureValidatetor() {
        return new DomainValidator<BlogEntryDomain>() {

            @Override public void validate(BlogEntryDomain domain) {
                addIfInvalid(StringUtils.isBlank(domain.getEntry()), "entry", FieldValidation.EMPTY);
                addIfInvalid(domain.getCreatorName() == null, "creatorName", FieldValidation.REQUIRED);
                addIfInvalid(domain.getBlog() == null, "blog", FieldValidation.REQUIRED);
            }
        };
    }

    @Override protected BlogEntryDomain buildDomain() {
        return new BlogEntryDomain(blogEntryEntityBuilder.build());
    }
}
