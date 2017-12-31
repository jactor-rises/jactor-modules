package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.persistence.dto.BlogDto;
import com.github.jactorrises.client.persistence.dto.BlogEntryDto;
import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.ValidInstance;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class BlogEntryBuilder extends AbstractBuilder<BlogEntryDomain> {
    private final BlogEntryDto blogEntryDto = new BlogEntryDto();

    BlogEntryBuilder() {
        super(BlogEntryBuilder::validateInstance);
    }

    BlogEntryBuilder withEntry(String entry) {
        blogEntryDto.setEntry(entry);
        return this;
    }


    BlogEntryBuilder withCreatorName(String creator) {
        blogEntryDto.setCreatorName(new Name(creator));
        return this;
    }

    public BlogEntryBuilder with(BlogDto blogDto) {
        blogEntryDto.setBlog(blogDto);
        return this;
    }

    private static Optional<String> validateInstance(BlogEntryDomain blogEntryDomain) {
        return ValidInstance.collectMessages(
                fetchMessageIfStringWithoutValue("entry", blogEntryDomain.getEntry()),
                fetchMessageIfFieldNotPresent("creatorName", blogEntryDomain.getCreatorName()),
                fetchMessageIfFieldNotPresent("blog", blogEntryDomain.getBlog())
        );
    }

    @Override protected BlogEntryDomain buildBean() {
        return new BlogEntryDomain(blogEntryDto);
    }
}
