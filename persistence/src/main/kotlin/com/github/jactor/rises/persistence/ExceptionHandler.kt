package com.github.jactor.rises.persistence

import com.github.jactor.rises.shared.finnFeiledeLinjer
import com.github.jactor.rises.shared.rootCauseSimpleMessage
import com.github.jactor.rises.shared.simpleExceptionMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
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

fun Throwable.exceptionMessageMedCause(): String {
    val exceptionMessage = exceptionMessage()
    val causeMessage = cause?.let { " - caused by ${it.findRootCauseMessage()}" } ?: ""

    return "$exceptionMessage$causeMessage".take(10000)
}

private fun Throwable.exceptionMessage() = when (this is HttpClientErrorException) {
    true -> "Internal client, $statusCode: ${simpleExceptionMessage()}"
    false -> simpleExceptionMessage()
}

private fun Throwable?.findRootCauseMessage(): String? {
    var rootCause: Throwable? = this

    while (rootCause?.cause != null) {
        rootCause = rootCause.cause
    }

    return rootCause?.simpleExceptionMessage()
}
