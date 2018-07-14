package com.gitlab.jactor.rises.io.rest;

import com.gitlab.jactor.rises.client.dto.BlogDto;
import com.gitlab.jactor.rises.client.dto.BlogEntryDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

public class BlogRestService extends AbstractRestService {

    public BlogRestService(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate, baseUrl);
    }

    public BlogDto saveOrUpdate(BlogDto blogDto) {
        ResponseEntity<BlogDto> responseEntity = exchangePost(
                fullUrl("/blog/persist"), new HttpEntity<>(blogDto), BlogDto.class
        );

        return bodyOf(responseEntity);
    }

    public BlogEntryDto saveOrUpdate(BlogEntryDto blogEntryDto) {
        ResponseEntity<BlogEntryDto> responseEntity = exchangePost(
                fullUrl("/blog/entry/persist"), new HttpEntity<>(blogEntryDto), BlogEntryDto.class
        );

        return bodyOf(responseEntity);
    }

    public BlogDto fetch(Serializable id) {
        ResponseEntity<BlogDto> responseEntity = getForEntity(fullUrl("/blog/get/" + id), BlogDto.class);

        return bodyOf(responseEntity);
    }

    public BlogEntryDto fetchEntry(Serializable entryId) {
        ResponseEntity<BlogEntryDto> responseEntity = getForEntity(fullUrl("/blog/entry/get/" + entryId), BlogEntryDto.class);

        return bodyOf(responseEntity);
    }
}
