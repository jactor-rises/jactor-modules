package com.github.jactorrises.client.dto;

import java.io.Serializable;
import java.util.Set;

public class GuestBookDto extends PersistentDto implements Serializable {
    private Set<GuestBookEntryDto> entries;
    private String title;
    private UserDto user;

    public GuestBookDto() {
        // empty, use setters
    }

    GuestBookDto(GuestBookDto guestBookDto) {
        super(guestBookDto);
        entries = guestBookDto.getEntries();
        title = guestBookDto.getTitle();
        user = guestBookDto.getUser();
    }

    public Set<GuestBookEntryDto> getEntries() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public UserDto getUser() {
        return user;
    }

    public void setEntries(Set<GuestBookEntryDto> entries) {
        this.entries = entries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
