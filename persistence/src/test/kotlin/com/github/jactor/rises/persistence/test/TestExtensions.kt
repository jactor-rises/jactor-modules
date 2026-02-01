package com.github.jactor.rises.persistence.test

import assertk.Assert
import assertk.assertThat
import java.time.Duration
import java.time.LocalDateTime

fun LocalDateTime.countSecondsUntilNow(): Long = Duration.between(this, LocalDateTime.now()).seconds
infix fun <T> T?.named(name: String): Assert<T?> = assertThat(actual = this, name = name)
