package com.github.jactorrises.client.persistence.dto;

import java.time.LocalDateTime;

public class GuestBookEntryDto extends PersistentDto {
    private GuestBookDto guestBook;
    private LocalDateTime createdTime;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getEntry() {
        return entry;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
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
