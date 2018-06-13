package com.github.jactor.rises.io.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("An AbstractRestService")
class AbstractRestServiceTest {

    @DisplayName("should throw exception when response entity is null")
    @Test void shouldThrowExceptionWhenNull() {
        assertThatIllegalStateException().isThrownBy(() -> (
                        new AbstractRestService(null) {
                        }
                ).bodyOf(null)
        ).withMessage("No response from REST service");
    }

    @DisplayName("should throw exception when response entity has a HttpStatus not in 2xx range")
    @ParameterizedTest(name = "Status not successful => {0}") @MethodSource("unsuccessfulHttpStatus")
    void shouldThrowExceptionWhenNotSuccessfulStatus(HttpStatus httpStatus) {
        assertThatIllegalStateException().isThrownBy(() -> (
                        new AbstractRestService(null) {
                        }
                ).bodyOf(new ResponseEntity<>(httpStatus))
        ).withMessageContaining("Bad configuration of REST service")
                .withMessageContaining("ResponseCode: " + httpStatus.name());
    }

    @DisplayName("should return body of response when response entity has a HttpStatus in 2xx range")
    @ParameterizedTest(name = "Status successful => {0}") @MethodSource("successfulHttpStatus")
    void shouldReturnBodyWhenSuccessfulStatus(HttpStatus httpStatus) {
        assertThat(
                (new AbstractRestService(null) {
                }).bodyOf(new ResponseEntity<>(new Object(), httpStatus))
        ).isNotNull();
    }

    private static Stream<HttpStatus> unsuccessfulHttpStatus() {
        return Arrays.stream(HttpStatus.values())
                .filter(httpStatus -> !httpStatus.is2xxSuccessful());
    }

    private static Stream<HttpStatus> successfulHttpStatus() {
        return Arrays.stream(HttpStatus.values())
                .filter(HttpStatus::is2xxSuccessful);
    }
}
