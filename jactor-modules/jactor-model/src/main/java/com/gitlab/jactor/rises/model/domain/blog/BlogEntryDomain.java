package com.gitlab.jactor.rises.model.domain.blog;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.model.domain.BlogEntry;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.model.domain.PersistentDomain;

import java.time.LocalDateTime;
import java.util.Optional;

public class BlogEntryDomain extends PersistentDomain implements BlogEntry {

    private final BlogEntryDto blogEntryDto;

    BlogEntryDomain(BlogEntryDto blogEntryDto) {
        this.blogEntryDto = blogEntryDto;
    }

    @Override public BlogDomain getBlog() {
        return Optional.ofNullable(blogEntryDto.getBlog()).map(BlogDomain::new).orElse(null);
    }

    @Override public LocalDateTime getCreatedTime() {
        return blogEntryDto.getCreationTime();
    }

    @Override public String getEntry() {
        return blogEntryDto.getEntry();
    }

    @Override public Name getCreatorName() {
        return Optional.ofNullable(blogEntryDto.getCreatorName()).map(Name::new).orElse(null);
    }

    @Override public BlogEntryDto getDto() {
        return blogEntryDto;
    }

    static BlogEntryBuilder aBlogEntry() {
        return new BlogEntryBuilder();
    }
}
