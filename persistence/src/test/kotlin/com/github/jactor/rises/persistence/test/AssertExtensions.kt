package com.github.jactor.rises.persistence.test

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isStrictlyBetween
import java.time.LocalDateTime

fun <T> Assert<T?>.all(function: T.() -> Unit) = isNotNull().given { assertk.assertAll { it.function() } }
infix fun Assert<String?>.contains(substring: String) = isNotNull().given { it.contains(substring.toRegex()) }
infix fun <T> Assert<T?>.equals(actual: T?): Unit = actual?.let { this.isEqualTo(it) } ?: this.isNull()
fun Assert<LocalDateTime>.isNotOlderThan(seconds: Long) = isStrictlyBetween(
    LocalDateTime.now().minusSeconds(seconds),
    LocalDateTime.now(),
)
