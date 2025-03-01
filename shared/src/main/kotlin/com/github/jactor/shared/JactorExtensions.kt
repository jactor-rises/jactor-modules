package com.github.jactor.shared

fun <T> Boolean.whenTrue(fn: () -> T?): T? = this.takeIf { this }?.let { fn() }

fun StackTraceElement.filEllerKlassenavn(): String {
    if (fileName == null) {
        return className
    }

    return fileName!!
}

fun StackTraceElement.kodelinjeEllerMetode(): String {
    if (lineNumber == -1) {
        return "metode:$methodName"
    }

    return "linje:$lineNumber"
}
