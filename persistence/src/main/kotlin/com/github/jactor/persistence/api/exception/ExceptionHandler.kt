package com.github.jactor.persistence.api.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import com.github.jactor.persistence.util.whenFalse
import com.github.jactor.persistence.util.whenTrue

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleGenericError(e: RuntimeException, request: WebRequest): ResponseEntity<Any>? =
        handleException(e, request, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any>? =
        handleException(e, request, HttpStatus.BAD_REQUEST)

    private fun handleException(
        e: RuntimeException,
        request: WebRequest,
        httpStatus: HttpStatus,
    ): ResponseEntity<Any>? {
        val message = exceptionMessageMedCause(e)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            put(HttpHeaders.WARNING, listOf(message))
        }

        // kLogger.warn { "${e.message.toString()}, $e" }
        return handleExceptionInternal(e, ErrorResponse(message), headers, httpStatus, request)
    }

    companion object {
        private fun exceptionMessage(t: Throwable) = if (t is HttpClientErrorException) {
            "Internal client, ${t.statusCode}: ${t.message}"
        } else {
            "${t::class.simpleName}: ${t.message}"
        }

        private fun findRootCauseMessage(cause: Throwable?): String? {
            var rootCause: Throwable? = cause

            while (rootCause?.cause != null) {
                rootCause = rootCause.cause
            }

            return rootCause?.let { "${it.javaClass.simpleName}: ${it.message}" }
        }

        fun exceptionMessageMedCause(throwable: Throwable): String {
            val exceptionMessage = exceptionMessage(throwable)
            val causeMessage = throwable.cause?.let { " - caused by ${findRootCauseMessage(it)}" } ?: ""

            return "$exceptionMessage$causeMessage".take(10000)
        }

        fun finnFeiledeLinjer(throwable: Throwable): List<String> {
            val linjer = mutableListOf<String>()
            var cause: Throwable? = throwable

            while (cause != null) {
                val stacks = cause.stackTrace
                val indent: String = (cause == throwable).whenFalse { "..." } ?: ""

                for (index in stacks.indices) {
                    val frame = stacks[index]
                    val isInternal = frame.className.startsWith("com.github.jactor")

                    if (isInternal || index == 0) {
                        linjer.add(
                            indent +
                                "${isInternal.whenTrue { "intern" } ?: "ekstern"}: " +
                                "${frame.filEllerKlassenavn()} " +
                                "(${frame.kodelinjeEllerMetode()})",
                        )
                    }
                }

                cause = cause.cause
            }

            return linjer
        }
    }

    @JvmRecord
    data class ErrorResponse(val errorMessage: String)
}

private fun StackTraceElement.filEllerKlassenavn(): String {
    if (fileName == null) {
        return className
    }

    return fileName!!
}

private fun StackTraceElement.kodelinjeEllerMetode(): String {
    if (lineNumber == -1) {
        return "metode:$methodName"
    }

    return "linje:$lineNumber"
}
