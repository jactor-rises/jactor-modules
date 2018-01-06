package com.github.jactorrises.client.persistence.dto;

import java.time.LocalDateTime;

public class BlogEntryDto extends PersistentDto {
    private BlogDto blog;
    private LocalDateTime createdTime;
    private String creatorName;
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

    public BlogDto getBlog() {
        return blog;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getEntry() {
        return entry;
    }

    public void setBlog(BlogDto blog) {
        this.blog = blog;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
