package com.github.jactor.rises.persistence.controller;

import com.github.jactor.rises.client.dto.PersistentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

class AbstractController {

    <T extends PersistentDto> ResponseEntity<T> aCreatedResponseEntity(T saved, String path) {
        try {
            return ResponseEntity.created(new URI(null, null, path, null)).body(saved);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(saved);
        }
    }
}
