package com.github.jactorrises.persistence.beans.service;

import com.github.jactorrises.client.persistence.dto.BlogDto;
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

    public BlogDto fetch(Serializable id) {
        ResponseEntity<BlogDto> responseEntity = getForEntity(baseUrl + '/' + id, BlogDto.class);

        return bodyOf(responseEntity);
    }

    public BlogDto saveOrUpdate(BlogDto blogDto) {
        if (blogDto.getId() == null) {
            return save(blogDto);
        }

        put(baseUrl + "/update", blogDto);

        return blogDto;
    }

    private BlogDto save(BlogDto blogDto) {
        ResponseEntity<BlogDto> responseEntity = exchange(
                baseUrl + "/save", HttpMethod.POST, new HttpEntity<>(blogDto), BlogDto.class
        );

        return bodyOf(responseEntity);
    }
}
