package com.github.jactorrises.persistence.service;

import com.github.jactorrises.client.persistence.dto.GuestBookDto;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;

import java.io.Serializable;

public class GuestBookDaoService {
    public GuestBookDto saveOrUpdate(GuestBookDto guestBookDto) {
        GuestBookDto persistedDto = new GuestBookDto();
        persistedDto.setId(guestBookDto.getId() != null ? guestBookDto.getId() : 1L);

        return persistedDto;
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        GuestBookEntryDto persistedDto = new GuestBookEntryDto();
        persistedDto.setId(guestBookEntryDto.getId() != null ? guestBookEntryDto.getId() : 1L);

        return persistedDto;
    }

    public <T> T fetch(Class<T> dtoClass, Serializable id) {
        throw new UnsupportedOperationException(String.format("WIP: %s, %s", dtoClass.getSimpleName(), id));
    }
}
