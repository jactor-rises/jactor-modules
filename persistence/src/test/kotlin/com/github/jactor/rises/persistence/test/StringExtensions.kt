package com.github.jactor.rises.persistence.test

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <T : Collection<String>> Assert<T>.containsSubstring(expected: String) = given { strings ->
    strings.forEach {
        if (it.contains(expected)) {
            return@given
        }
    }

    expected("to contain substring:${show(expected)}, but list was:${show(strings)}")
}
