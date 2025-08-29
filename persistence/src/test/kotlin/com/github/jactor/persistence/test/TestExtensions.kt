package com.github.jactor.persistence.test

import assertk.Assert
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <T> Assert<T?>.all(function: T.() -> Unit) = isNotNull().given { assertAll { it.function() } }
infix fun Assert<String?>.contains(substring: String) = isNotNull().given { it.contains(substring.toRegex()) }
infix fun <T> Assert<T?>.equals(actual: T?): Unit = when(actual == null) {
    true -> this.isNull()
    false -> this.isEqualTo(actual)
}

infix fun <T> T?.named(name: String): Assert<T?> = assertThat(actual = this, name = name)
fun Assert<List<String>>.containsSubstring(expected: String) = given { strings ->
    strings.forEach {
        if (it.contains(expected)) {
            return@given
        }
    }

    expected("to contain substring:${show(expected)}, but list was:${show(strings)}")
}
