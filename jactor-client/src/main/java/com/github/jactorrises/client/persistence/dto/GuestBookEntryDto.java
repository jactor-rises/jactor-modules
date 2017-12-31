package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;

import java.time.LocalDateTime;

public class GuestBookEntryDto extends PersistentDto implements GuestBookEntry {
    private GuestBookDto guestBook;
    private LocalDateTime createdTime;
    private Name creatorName;
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

    @Override public GuestBookDto getGuestBook() {
        return guestBook;
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

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatorName(Name creatorNmae) {
        this.creatorName = creatorNmae;
    }

    public void setGuestBook(GuestBookDto guestBook) {
        this.guestBook = guestBook;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
