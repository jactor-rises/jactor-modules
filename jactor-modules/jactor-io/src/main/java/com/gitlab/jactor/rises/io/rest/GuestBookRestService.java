package com.gitlab.jactor.rises.io.rest;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

public class GuestBookRestService extends AbstractRestService {

    public GuestBookRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate, baseUrl);
    }

    public GuestBookDto saveOrUpdate(GuestBookDto guestBookDto) {
        ResponseEntity<GuestBookDto> responseEntity = exchangePost(
                fullUrl("/guestBook/persist"), new HttpEntity<>(guestBookDto), GuestBookDto.class
        );

        return bodyOf(responseEntity);
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        ResponseEntity<GuestBookEntryDto> responseEntity = exchangePost(
                fullUrl("/guestBook/entry/persist"), new HttpEntity<>(guestBookEntryDto), GuestBookEntryDto.class
        );

        return bodyOf(responseEntity);
    }

    public GuestBookDto fetch(Serializable id) {
        ResponseEntity<GuestBookDto> responseEntity = getForEntity(fullUrl("/guestBook/get/" + id), GuestBookDto.class);

        return bodyOf(responseEntity);
    }

    public GuestBookEntryDto fetchEntry(Serializable entryId) {
        ResponseEntity<GuestBookEntryDto> responseEntity = getForEntity(fullUrl("/guestBook/entry/get/" + entryId), GuestBookEntryDto.class);

        return bodyOf(responseEntity);
    }
}
