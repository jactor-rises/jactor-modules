package com.github.jactor.rises.persistence

import com.github.jactor.rises.shared.exceptionMessageMedCause
import com.github.jactor.rises.shared.finnFeiledeLinjer
import com.github.jactor.rises.shared.rootCauseSimpleMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(RuntimeException::class)
    fun handleGenericError(e: RuntimeException): Mono<ResponseEntity<Any>> {
        logger.warn { "${e.rootCauseSimpleMessage()}: ${e.finnFeiledeLinjer().joinToString { ", " }}" }

        return Mono.just(
            ResponseEntity
                .internalServerError()
                .header(HttpHeaders.WARNING, "Internal Server Error! ${e.exceptionMessageMedCause()}!")
                .build(),
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): Mono<ResponseEntity<Any>> {
        logger.warn { "${e.rootCauseSimpleMessage()}: ${e.finnFeiledeLinjer().joinToString { ", " }}" }

        return Mono.just(
            ResponseEntity
                .badRequest()
                .header(HttpHeaders.WARNING, "Bad Request: ${e.exceptionMessageMedCause()}!")
                .build(),
        )
    }
}
