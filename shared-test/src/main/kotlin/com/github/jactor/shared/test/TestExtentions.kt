package com.github.jactor.shared.test

import assertk.Assert
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

fun <T> Assert<T>.assertAll(asserts: (T) -> Unit): Unit = given { asserts(it) }
infix fun <T> Assert<T>.equalTo(ectual: T): Unit = this.isEqualTo(ectual)
infix fun <T : Collection<*>> Assert<T>.withSize(size: Int): Unit = this.hasSize(size = size)
infix fun <T> T.named(name: String): Assert<T> = assertThat(actual = this, name = name)
