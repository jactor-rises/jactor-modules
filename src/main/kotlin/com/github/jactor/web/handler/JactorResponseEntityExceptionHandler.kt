package com.github.jactor.web.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.Optional
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

@ControllerAdvice
class JactorResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JactorResponseEntityExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleInternalServerError(rex: RuntimeException, headers: HttpHeaders?, webRequest: WebRequest): ResponseEntity<Any> {
        logException(rex, webRequest)
        logCause(rex.cause)

        return handleExceptionInternal(rex, null, headers!!, HttpStatus.INTERNAL_SERVER_ERROR, webRequest)
    }

    private fun logException(throwable: Throwable, webRequest: WebRequest) {
        LOGGER.error("Exception caught in {} {}", webRequest.contextPath, webRequest.getDescription(true))
        LOGGER.error("Failed by {}: {}", throwable::javaClass.name, throwable.message)

        logCause(throwable.cause)

        StackWalker.getInstance().walk { stackFrameStream: Stream<StackWalker.StackFrame> ->
            stackFrameStream.filter { stackFrame: StackWalker.StackFrame ->
                stackFrame.className.startsWith("com.github.jactor")
            }
                .skip(1) // skip this method
                .collect(Collectors.toList())
        }.forEach(
            Consumer { stackFrame: StackWalker.StackFrame ->
                LOGGER.error(" - ${stackFrame.className}(line:${stackFrame.lineNumber}) - ${stackFrame.fileName}")
            }
        )
    }

    private fun logCause(cause: Throwable?) {
        var possibleCause: Optional<Throwable> = Optional.ofNullable(cause)

        while (possibleCause.isPresent) {
            val theCause = possibleCause.get()
            LOGGER.error("  ...caused by ${theCause.javaClass.name}: ${theCause.message} ", theCause)
            possibleCause = Optional.ofNullable(theCause.cause)
        }
    }
}