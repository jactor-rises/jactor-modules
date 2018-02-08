package com.github.jactorrises.persistence.orm.controller;

import com.github.jactor.rises.client.dto.PersistentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

class AbstractController {

    <T extends PersistentDto> ResponseEntity<T> createdWithLocation(T saved, String localtion) {
        try {
            return ResponseEntity.created(new URI(null, null, localtion + saved.getId(), null)).body(saved);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
