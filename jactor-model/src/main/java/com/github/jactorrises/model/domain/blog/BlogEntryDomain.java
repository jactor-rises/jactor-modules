package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.client.persistence.dto.BlogEntryDto;
import com.github.jactorrises.model.domain.PersistentDomain;

import java.time.LocalDateTime;

public class BlogEntryDomain extends PersistentDomain<Long> implements BlogEntry {

    private final BlogEntryDto blogEntryDto;

    BlogEntryDomain(BlogEntryDto blogEntryDto) {
        this.blogEntryDto = blogEntryDto;
    }

    @Override public BlogDomain getBlog() {
        return blogEntryDto.getBlog() != null ? new BlogDomain(blogEntryDto.getBlog()) : null;
    }

    @Override public LocalDateTime getCreatedTime() {
        return blogEntryDto.getCreatedTime();
    }

    @Override public String getEntry() {
        return blogEntryDto.getEntry();
    }

    @Override public Name getCreatorName() {
        return blogEntryDto.getCreatorName();
    }

    @Override public BlogEntryDto getDto() {
        return blogEntryDto;
    }

    static BlogEntryBuilder aBlogEntry() {
        return new BlogEntryBuilder();
    }
}
