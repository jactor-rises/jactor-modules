package com.github.jactor.persistence.api.exception

import java.util.UUID
import kotlin.test.fail
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.api.controller.UserController
import com.github.jactor.persistence.user.UserRepository
import com.github.jactor.persistence.user.UserService
import com.github.jactor.persistence.test.containsSubstring
import com.github.jactor.shared.finnFeiledeLinjer
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk

internal class ExceptionHandlerTest {
    @Test
    fun `skal hente kodelinjer fra vår kode når exception oppstår`() {
        val repositoryMockk = mockk<UserRepository> {}
        val avstemmingController = UserController(
            userService = UserService(personService = mockk {}, userRepository = repositoryMockk),
        )

        every { repositoryMockk.findById(any()) } answers { error("boom!") }

        runCatching {
            avstemmingController.get(id = UUID.randomUUID())
        }.onSuccess {
            fail("Kjøring skulle feilet!")
        }.onFailure {
            val kodelinjer = it.finnFeiledeLinjer()

            assertAll {
                assertThat(kodelinjer).containsSubstring("intern: UserController.kt (linje:")
                assertThat(kodelinjer).containsSubstring("intern: UserService.kt (linje:")
            }
        }
    }

    @Test
    fun `skal skrive klassenavn når filnavn ikke er tilstede`() {
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
    fun `skal skrive metodenavn når kodelinje mangler`() {
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
