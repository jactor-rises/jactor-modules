package com.github.jactor.shared.test

import java.time.LocalDateTime
import assertk.Assert
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isStrictlyBetween
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <T> Assert<T?>.all(function: T.() -> Unit) = isNotNull().given { assertk.assertAll { it.function() } }
infix fun Assert<String?>.contains(substring: String) = isNotNull().given { it.contains(substring.toRegex()) }
infix fun <T> Assert<T?>.equals(actual: T?): Unit = actual?.let { this.isEqualTo(it) } ?: this.isNull()
fun <T: Collection<String>> Assert<T>.containsSubstring(expected: String) = given { strings ->
    strings.forEach {
        if (it.contains(expected)) {
            return@given
        }
    }

    expected("to contain substring:${show(expected)}, but list was:${show(strings)}")
}

fun Assert<LocalDateTime>.isNotOlderThan(seconds: Long) = isStrictlyBetween(
    LocalDateTime.now().minusSeconds(seconds), LocalDateTime.now()
)

infix fun <T : Collection<*>> Assert<T?>.sized(size: Int): Unit = this.isNotNull().hasSize(size = size)
infix fun <T> T?.named(name: String): Assert<T?> = assertThat(actual = this, name = name)
