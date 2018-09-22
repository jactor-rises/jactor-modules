package com.gitlab.jactor.rises.persistence.controller;

import com.gitlab.jactor.rises.commons.dto.AddressDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("An AbstractController")
class AbstractControllerTest {

    @DisplayName("should return a response containing HTTP_STATUS 500 when the path is not according to the RFC 2396 specification")
    @Test void shouldRespondWithInternalErrorWhenSyntaxIsWrong() {
        ResponseEntity<AddressDto> toCreate = new AbstractController() {
        }
                .aCreatedResponseEntity(new AddressDto(), ":path:with.errors...");

        assertThat(toCreate.getStatusCode()).as("create response http status")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
