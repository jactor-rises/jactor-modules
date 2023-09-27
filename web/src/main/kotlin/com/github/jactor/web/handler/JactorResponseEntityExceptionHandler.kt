package com.github.jactor.web.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class JactorResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JactorResponseEntityExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleInternalServerError(
        rex: RuntimeException,
        headers: HttpHeaders?,
        webRequest: WebRequest
    ): ResponseEntity<Any>? {
        logException(rex, webRequest)
        logCause(rex.cause)

        return handleExceptionInternal(rex, null, headers!!, HttpStatus.INTERNAL_SERVER_ERROR, webRequest)
    }

    private fun logException(throwable: Throwable, webRequest: WebRequest) {
        LOGGER.error("Exception caught in {} {}", webRequest.contextPath, webRequest.getDescription(true))
        LOGGER.error("Failed by {}: {}", throwable::javaClass.name, throwable.message)

        logCause(throwable.cause)

        StackWalker.getInstance().walk { frames ->
            frames.filter { it.className.startsWith("com.github.jactor") }
                .skip(1) // skip this method
                .toList()
        }.forEach {
            LOGGER.error(" - ${it.className}(line:${it.lineNumber}) - ${it.fileName}")
        }
    }

    private fun logCause(cause: Throwable?) {
        var possibleCause: Throwable? = cause

        while (possibleCause != null) {
            LOGGER.error("  ...caused by ${possibleCause.javaClass.name}: ${possibleCause.message} ", possibleCause)
            possibleCause = possibleCause.cause
        }
    }
}