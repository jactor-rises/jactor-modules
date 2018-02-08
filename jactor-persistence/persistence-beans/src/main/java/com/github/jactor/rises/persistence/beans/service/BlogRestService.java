package com.github.jactor.rises.persistence.beans.service;

import com.github.jactorrises.client.dto.BlogDto;
import com.github.jactorrises.client.dto.BlogEntryDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

public class BlogRestService extends AbstractRestService {

    private final String baseUrl;

    public BlogRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public BlogDto saveOrUpdate(BlogDto blogDto) {
        ResponseEntity<BlogDto> responseEntity = exchange(
                baseUrl + "/persist", HttpMethod.POST, new HttpEntity<>(blogDto), BlogDto.class
        );

        return bodyOf(responseEntity);
    }

    public BlogEntryDto saveOrUpdate(BlogEntryDto blogEntryDto) {
        ResponseEntity<BlogEntryDto> responseEntity = exchange(
                baseUrl + "/entry/persist", HttpMethod.POST, new HttpEntity<>(blogEntryDto), BlogEntryDto.class
        );

        return bodyOf(responseEntity);
    }

    public BlogDto fetch(Serializable id) {
        ResponseEntity<BlogDto> responseEntity = getForEntity(baseUrl + '/' + id, BlogDto.class);

        return bodyOf(responseEntity);
    }

    public BlogEntryDto fetchEntry(Serializable entryId) {
        ResponseEntity<BlogEntryDto> responseEntity = getForEntity(baseUrl + "/entry/" + entryId, BlogEntryDto.class);

        return bodyOf(responseEntity);
    }
}
