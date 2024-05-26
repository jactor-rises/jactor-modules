package com.github.jactor.persistence.util

fun <T> Boolean.whenTrue(callback: () -> T): T? = when (this) {
    true -> callback()
    false -> null
}

fun <T> Boolean.whenFalse(callback: () -> T): T? = when (this) {
    true -> null
    false -> callback()
}