package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.domain.GuestBook;

public class GuestBookDto extends PersistentDto implements GuestBook {
    private String title;
    private UserDto user;

    public GuestBookDto() {
        // empty, use setters
    }

    GuestBookDto(GuestBookDto guestBookDto) {
        super(guestBookDto);
        title = guestBookDto.getTitle();
        user = guestBookDto.getUser();
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public UserDto getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
