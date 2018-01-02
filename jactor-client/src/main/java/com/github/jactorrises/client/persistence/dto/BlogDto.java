package com.github.jactorrises.client.persistence.dto;

import java.time.LocalDate;

public class BlogDto extends PersistentDto {
    private LocalDate created;
    private String title;
    private UserDto user;

    public BlogDto() {
        // empty, use setters
    }

    public BlogDto(BlogDto blogDto) {
        super(blogDto);
        created = blogDto.getCreated();
        title = blogDto.getTitle();
        user = blogDto.getUser();
    }

    public LocalDate getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public UserDto getUser() {
        return user;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
