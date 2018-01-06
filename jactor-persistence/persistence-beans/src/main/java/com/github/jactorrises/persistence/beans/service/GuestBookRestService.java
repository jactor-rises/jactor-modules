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
        if (guestBookDto.getId() == null) {
            return save(guestBookDto);
        }

        put(baseUrl + "/update", guestBookDto);

        return guestBookDto;
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        if (guestBookEntryDto.getId() == null) {
            return save(guestBookEntryDto);
        }

        put(baseUrl + "/entry/update", guestBookEntryDto);

        return guestBookEntryDto;
    }

    private GuestBookDto save(GuestBookDto guestBookDto) {
        ResponseEntity<GuestBookDto> responseEntity = exchange(
                baseUrl + "/save", HttpMethod.POST, new HttpEntity<>(guestBookDto), GuestBookDto.class
        );

        return bodyOf(responseEntity);
    }

    private GuestBookEntryDto save(GuestBookEntryDto guestBookEntryDto) {
        ResponseEntity<GuestBookEntryDto> responseEntity = exchange(
                baseUrl + "/entry/save", HttpMethod.POST, new HttpEntity<>(guestBookEntryDto), GuestBookEntryDto.class
        );

        return bodyOf(responseEntity);
    }

    public GuestBookDto fetch(Serializable id) {
        ResponseEntity<GuestBookDto> responseEntity = getForEntity(baseUrl + '/' + id, GuestBookDto.class);

        return bodyOf(responseEntity);
    }

    public GuestBookEntryDto fetchEntry(Serializable id) {
        ResponseEntity<GuestBookEntryDto> responseEntity = getForEntity(baseUrl + "/entry/" + id, GuestBookEntryDto.class);

        return bodyOf(responseEntity);
    }
}
