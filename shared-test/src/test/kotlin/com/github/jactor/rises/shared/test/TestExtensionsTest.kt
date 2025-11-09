package com.github.jactor.rises.shared.test

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isFailure
import org.junit.jupiter.api.Test

class TestExtensionsTest {
    @Test
    fun `should succeed when test equals on null`() {
        val any: Any? = null

        assertThat(any).given {
            it named "any" equals null
        }
    }

    @Test
    fun `should fail when testing an instance which is null`() {
        val shouldFail = runCatching { assertThat(null).all { } }

        assertThat(shouldFail).isFailure().given {
            it::class.java.name named "full class name contains" contains "assertk"
            it.message named "failure message" contains "not be null"
        }
    }

    @Test
    fun `should succeed test with several asserts on one instance`() {
        val action = "shit" to "hits the fan"

        assertThat(action).all {
            first named "what" equals "shit"
            second named "action" equals "hits the fan"
        }
    }

    @Test
    fun `should check that substring is present in a list of string`() {
        val list = listOf(
            "one ring to rule them all",
            "one ring to find them",
            "one ring to bring them all and in the darkness bind them"
        )

        assertThat(list).containsSubstring("darkness")
    }

    @Test
    fun `should fail when check of substring is not present in a list of string`() {
        val list = listOf(
            "one ring to rule them all",
            "one ring to find them",
            "one ring to bring them all and in the darkness bind them"
        )

        assertFailure {
            assertThat(list).containsSubstring("bind some")
        }
    }

    @Test
    fun `should pass when collection has expected size`() {
        assertThat(listOf("a", "b", "c")).given {
            it named "list" sized 3
        }
    }

    @Test
    fun `should fail when collection does not have expected size`() {
        val list = listOf("a", "b")

        assertFailure {
            assertThat(list).sized(3)
        }
    }
}
