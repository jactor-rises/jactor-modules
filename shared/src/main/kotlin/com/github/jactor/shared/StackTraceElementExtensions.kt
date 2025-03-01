package com.github.jactor.shared

fun StackTraceElement.filnavnEllerKlassenavn(): String = fileName ?: className
fun StackTraceElement.kodelinjeEllerMetode(): String = (lineNumber == -1).whenTrue { "metode:$methodName" }
    ?: "linje:$lineNumber"
