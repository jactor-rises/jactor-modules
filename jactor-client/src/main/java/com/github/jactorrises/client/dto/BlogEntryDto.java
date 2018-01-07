package com.github.jactorrises.client.dto;

import java.io.Serializable;

public class BlogEntryDto extends PersistentDto implements Serializable {
    private BlogDto blog;
    private String createdTime;
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

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getEntry() {
        return entry;
    }

    public void setBlog(BlogDto blog) {
        this.blog = blog;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
