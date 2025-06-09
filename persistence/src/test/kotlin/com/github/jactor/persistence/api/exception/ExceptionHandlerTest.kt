package com.github.jactor.persistence.api.exception

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.api.controller.UserController
import com.github.jactor.persistence.test.containsSubstring
import com.github.jactor.persistence.user.UserRepository
import com.github.jactor.persistence.user.UserService
import com.github.jactor.shared.finnFeiledeLinjer
import assertk.assertAll
import assertk.assertThat
import assertk.fail
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
                assertThat(kodelinjer).containsSubstring("intern: Controllers.kt (linje:")
                assertThat(kodelinjer).containsSubstring("intern: UserService.kt (linje:")
            }
        }
    }
}
