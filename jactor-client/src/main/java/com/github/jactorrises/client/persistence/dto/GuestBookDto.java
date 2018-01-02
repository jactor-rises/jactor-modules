package com.github.jactorrises.client.persistence.dto;

public class GuestBookDto extends PersistentDto {
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

    public String getTitle() {
        return title;
    }

    public UserDto getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
