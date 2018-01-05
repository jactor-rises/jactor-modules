package com.github.jactorrises.persistence.beans.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class AbstractRestService {
    private final RestTemplate restTemplate;

    AbstractRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    <T> ResponseEntity<T> getForEntity(String url, Class<T> requstedClass) {
        return restTemplate.getForEntity(url, requstedClass);
    }

    <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<T> httpEntity, Class<T> requstedClass) {
        return restTemplate.exchange(url, httpMethod, httpEntity, requstedClass);
    }

    void put(String url, Object updateObject) {
        restTemplate.put(url, updateObject);
    }

    <T> T bodyOf(ResponseEntity<T> responseEntity) {
        if (responseEntity == null || isNot2xxSuccessful(responseEntity.getStatusCode())) {
            throw new IllegalStateException("Bad configuration of REST service");
        }

        return responseEntity.getBody();
    }

    private boolean isNot2xxSuccessful(HttpStatus statusCode) {
        return !statusCode.is2xxSuccessful();
    }
}
