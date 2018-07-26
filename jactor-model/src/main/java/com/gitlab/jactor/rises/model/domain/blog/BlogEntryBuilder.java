package com.gitlab.jactor.rises.model.domain.blog;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;

import java.util.Optional;

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

    private static Optional<MissingFields> validateInstance(BlogEntryDomain blogEntryDomain, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenBlank("entry", blogEntryDomain.getEntry());
        missingFields.addInvalidFieldWhenNoValue("creatorName", blogEntryDomain.getCreatorName());
        missingFields.addInvalidFieldWhenNoValue("blog", blogEntryDomain.getBlog());

        return missingFields.presentWhenFieldsAreMissing();
    }

    @Override protected BlogEntryDomain buildBean() {
        return new BlogEntryDomain(blogEntryDto);
    }
}
