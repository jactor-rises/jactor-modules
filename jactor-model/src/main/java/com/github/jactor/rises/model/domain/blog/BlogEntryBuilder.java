package com.github.jactor.rises.model.domain.blog;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.commons.builder.ValidInstance;

import java.util.Optional;

import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

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
        blogEntryDto.setCreatorName(creator);
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
