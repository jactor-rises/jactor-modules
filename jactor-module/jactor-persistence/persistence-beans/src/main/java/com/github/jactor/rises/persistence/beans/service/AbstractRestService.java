package com.github.jactor.rises.persistence.beans.service;

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
        if (responseEntity == null) {
            throw new IllegalStateException("No response from REST service");
        }

        if (isNot2xxSuccessful(responseEntity.getStatusCode())) {
            String badConfiguredResponseMesssage = String.format("Bad configuration of REST service! ResponseCode: %s(%d)",
                    responseEntity.getStatusCode().name(),
                    responseEntity.getStatusCodeValue()
            );

            throw new IllegalStateException(badConfiguredResponseMesssage);
        }

        return responseEntity.getBody();
    }

    private boolean isNot2xxSuccessful(HttpStatus statusCode) {
        return !statusCode.is2xxSuccessful();
    }
}
