package com.github.jactor.shared

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk

@Disabled("må skrives om siden den mocker kode fra JVM")
class ThrowableExtensionsTest {
    @Test
    fun `skal bruke klassenavn når filnavn ikke er tilstede`() {
        val throwableMock = mockk<Throwable>(relaxed = true) {
            every { cause } returns null
            every { stackTrace } returns arrayOf(
                StackTraceElement(
                    "com.github.jactor.package.MyService",
                    "run",
                    null,
                    15,
                ),
            )
        }

        val linjer = throwableMock.finnFeiledeLinjer()

        assertThat(linjer).isEqualTo(listOf("intern: com.github.jactor.package.MyService (linje:15)"))
    }

    @Test
    fun `skal bruke metodenavn når kodelinje mangler`() {
        val throwableMock = mockk<Throwable>(relaxed = true) {
            every { cause } returns null
            every { stackTrace } returns arrayOf(
                StackTraceElement(
                    "com.github.jactor.package.MyService",
                    "run",
                    null,
                    -1,
                ),
            )
        }

        val linjer = throwableMock.finnFeiledeLinjer()

        assertThat(linjer).isEqualTo(listOf("intern: com.github.jactor.package.MyService (metode:run)"))
    }
}