package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.persistence.dto.GuestBookDto;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

public class GuestBookDaoService {
    private final RestTemplate restTemplate;

    public GuestBookDaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
