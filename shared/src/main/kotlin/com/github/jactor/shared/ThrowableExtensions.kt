package com.github.jactor.shared

import org.springframework.web.client.HttpClientErrorException

fun Throwable.exceptionMessageMedCause(): String {
    val exceptionMessage = exceptionMessage()
    val causeMessage = cause?.let { " - caused by ${it.findRootCauseMessage()}" } ?: ""

    return "$exceptionMessage$causeMessage".take(10000)
}

fun Throwable.exceptionMessage() = when (this is HttpClientErrorException) {
    true -> "Internal client, $statusCode: $message"
    false -> simpleExceptionMessage()
}

private fun Throwable?.findRootCauseMessage(): String? {
    var rootCause: Throwable? = this

    while (rootCause?.cause != null) {
        rootCause = rootCause.cause
    }

    return rootCause?.simpleExceptionMessage()
}

private fun Throwable.simpleExceptionMessage(): String = "${this::class.simpleName}: ${message}"

fun Throwable.finnFeiledeLinjer(): List<String> = buildList{
    var cause: Throwable? = this@finnFeiledeLinjer

    while (cause != null) {
        val stacks = cause.stackTrace
        val indent: String = (cause == this).whenFalse { "..." } ?: ""

        for (index in stacks.indices) {
            val frame = stacks[index]
            val isInternal = frame.className.startsWith("com.github.jactor")

            (isInternal || index == 0).whenTrue {
                val internVsEkstern = isInternal.whenTrue { "intern" } ?: "ekstern"
                val filnavnEllerKlasse = frame.filnavnEllerKlassenavn()
                val linjeEllerMetode = frame.kodelinjeEllerMetode()

                add("$indent$internVsEkstern: $filnavnEllerKlasse ($linjeEllerMetode)")
            }
        }

        cause = cause.cause
    }
}
