package com.github.jactor.rises.shared

fun Throwable.findOrginCause(): Throwable = generateSequence(this) { it.cause }.last()
fun Throwable.rootCauseSimpleMessage(): String = findOrginCause().simpleExceptionMessage()
fun Throwable.simpleExceptionMessage(): String = "${this::class.simpleName}: $message"

fun Throwable.finnFeiledeLinjer(): List<String> = buildList {
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
