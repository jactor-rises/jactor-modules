package com.github.jactor.persistence

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.github.jactor.shared.exceptionMessageMedCause
import com.github.jactor.shared.finnFeiledeLinjer
import com.github.jactor.shared.originClassNameEndsWith
import com.github.jactor.shared.rootCauseSimpleMessage
import com.github.jactor.shared.whenTrue
import io.github.oshai.kotlinlogging.KotlinLogging
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
                .build()
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): Mono<ResponseEntity<Any>> {
        return e.originClassNameEndsWith(txt = "Controller").whenTrue {
            logger.warn { "${e.rootCauseSimpleMessage()}: ${e.finnFeiledeLinjer().joinToString { ", " }}" }

            Mono.just(
                ResponseEntity
                    .badRequest()
                    .header(HttpHeaders.WARNING, "Bad Request: ${e.exceptionMessageMedCause()}!")
                    .build()
            )
        } ?: handleGenericError(e)
    }
}
