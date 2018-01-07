package com.github.jactorrises.client.dto;

import java.io.Serializable;

public class GuestBookEntryDto extends PersistentDto implements Serializable {
    private GuestBookDto guestBook;
    private String createdTime;
    private String creatorName;
    private String entry;

    public GuestBookEntryDto() {
        // empty, use setters
    }

    GuestBookEntryDto(GuestBookEntryDto guestBookEntryDto) {
        super(guestBookEntryDto);
        guestBook = guestBookEntryDto.getGuestBook();
        createdTime = guestBookEntryDto.getCreatedTime();
        creatorName = guestBookEntryDto.getCreatorName();
        entry = guestBookEntryDto.getEntry();
    }

    public GuestBookDto getGuestBook() {
        return guestBook;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getEntry() {
        return entry;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatorName(String creatorNmae) {
        this.creatorName = creatorNmae;
    }

    public void setGuestBook(GuestBookDto guestBook) {
        this.guestBook = guestBook;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
