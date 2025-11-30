package com.github.jactor.rises.web.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val kLogger = KotlinLogging.logger {}

@ControllerAdvice
class JactorResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [RuntimeException::class])
    fun handleInternalServerError(
        runtimeException: RuntimeException,
        headers: HttpHeaders?,
        webRequest: WebRequest,
    ): ResponseEntity<Any>? = logException(runtimeException, webRequest).let {
        handleExceptionInternal(
            runtimeException,
            null,
            headers ?: HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            webRequest,
        )
    }

    private fun logException(throwable: Throwable, webRequest: WebRequest) {
        val contextPath = webRequest.contextPath
        val description = webRequest.getDescription(true)

        kLogger.error(throwable) { "Exception caught in $contextPath $description" }
        kLogger.error { "Failed by ${throwable::javaClass.name}: ${throwable.message}" }

        StackWalker.getInstance().walk { frames ->
            frames.filter { it.className.startsWith("com.github.jactor") }
                .skip(1) // skip this method
                .toList()
        }.forEach {
            logger.error { " - ${it.className}(line:${it.lineNumber}) - ${it.fileName}" }
        }
    }
}
