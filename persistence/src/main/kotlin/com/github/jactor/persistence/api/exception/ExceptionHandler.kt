package com.github.jactor.persistence.api.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import com.github.jactor.shared.exceptionMessageMedCause
import com.github.jactor.shared.finnFeiledeLinjer
import io.github.oshai.kotlinlogging.KotlinLogging

private val kLogger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleGenericError(e: RuntimeException, request: WebRequest): ResponseEntity<Any>? =
        handleException(e, request, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any>? =
        handleException(e, request, HttpStatus.BAD_REQUEST)

    private fun handleException(
        runtimeException: RuntimeException,
        webRequest: WebRequest,
        httpStatus: HttpStatus,
    ): ResponseEntity<Any>? = runtimeException.exceptionMessageMedCause().let { message ->
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            put(HttpHeaders.WARNING, listOf(message))
        }

        kLogger.error(runtimeException) {
            "$message: ${runtimeException.finnFeiledeLinjer().joinToString { ", " }}"
        }

        handleExceptionInternal(runtimeException, ErrorResponse(message), headers, httpStatus, webRequest)
    }

    @JvmRecord
    data class ErrorResponse(val errorMessage: String)
}
