package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.dto.GuestBookDto;
import com.github.jactorrises.client.dto.GuestBookEntryDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

public class GuestBookRestService extends AbstractRestService {

    private final String baseUrl;

    public GuestBookRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public GuestBookDto saveOrUpdate(GuestBookDto guestBookDto) {
        ResponseEntity<GuestBookDto> responseEntity = exchange(
                baseUrl + "/persist", HttpMethod.POST, new HttpEntity<>(guestBookDto), GuestBookDto.class
        );

        return bodyOf(responseEntity);
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        ResponseEntity<GuestBookEntryDto> responseEntity = exchange(
                baseUrl + "/entry/persist", HttpMethod.POST, new HttpEntity<>(guestBookEntryDto), GuestBookEntryDto.class
        );

        return bodyOf(responseEntity);
    }

    public GuestBookDto fetch(Serializable id) {
        ResponseEntity<GuestBookDto> responseEntity = getForEntity(baseUrl + '/' + id, GuestBookDto.class);

        return bodyOf(responseEntity);
    }

    public GuestBookEntryDto fetchEntry(Serializable entryId) {
        ResponseEntity<GuestBookEntryDto> responseEntity = getForEntity(baseUrl + "/entry/" + entryId, GuestBookEntryDto.class);

        return bodyOf(responseEntity);
    }
}
