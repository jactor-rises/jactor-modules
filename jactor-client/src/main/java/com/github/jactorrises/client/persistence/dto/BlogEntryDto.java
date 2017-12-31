package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;

import java.time.LocalDateTime;

public class BlogEntryDto extends PersistentDto implements BlogEntry {
    private BlogDto blog;
    private LocalDateTime createdTime;
    private Name creatorName;
    private String entry;

    public BlogEntryDto() {
        // empty, use setters
    }

    BlogEntryDto(BlogEntryDto blogEntryDto) {
        super(blogEntryDto);
        blog = blogEntryDto.getBlog();
        createdTime = blogEntryDto.getCreatedTime();
        creatorName = blogEntryDto.getCreatorName();
        entry = blogEntryDto.getEntry();
    }

    @Override public BlogDto getBlog() {
        return blog;
    }

    @Override public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override public Name getCreatorName() {
        return creatorName;
    }

    @Override public String getEntry() {
        return entry;
    }

    public void setBlog(BlogDto blog) {
        this.blog = blog;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatorName(Name creatorName) {
        this.creatorName = creatorName;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
