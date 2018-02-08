package com.github.jactor.rises.client.dto;

import java.io.Serializable;
import java.util.Set;

public class BlogDto extends PersistentDto implements Serializable {
    private Set<BlogEntryDto> entries;
    private String created;
    private String title;
    private UserDto user;

    public BlogDto() {
        // empty, use setters
    }

    public BlogDto(BlogDto blogDto) {
        super(blogDto);
        created = blogDto.getCreated();
        entries = blogDto.getEntries();
        title = blogDto.getTitle();
        user = blogDto.getUser();
    }

    public String getCreated() {
        return created;
    }

    public Set<BlogEntryDto> getEntries() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public UserDto getUser() {
        return user;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setEntries(Set<BlogEntryDto> entries) {
        this.entries = entries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
