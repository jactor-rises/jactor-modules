package com.github.jactor.rises.persistence

import assertk.all
import assertk.assertThat
import assertk.assertions.isTrue
import assertk.fail
import com.github.jactor.rises.persistence.user.UserController
import com.github.jactor.rises.persistence.user.UserRepository
import com.github.jactor.rises.persistence.user.UserService
import com.github.jactor.rises.shared.finnFeiledeLinjer
import com.github.jactor.rises.shared.test.containsSubstring
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class ExceptionHandlerTest {
    private val exceptionHandler = ExceptionHandler()

    @Test
    fun `skal være bad request når IllegalArgumentException oppstår`() {
        val response = exceptionHandler.handleBadRequest(e = TestController().illegalArgumentException()).block()

        assertThat(response!!.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)).isTrue()
    }

    @Test
    fun `skal hente kodelinjer fra vår kode når exception oppstår`() = runTest {
        val userRepositoryMockk = mockk<UserRepository> {}
        val avstemmingController = UserController(
            userService = UserService(
                userRepository = userRepositoryMockk
            )
        )

        every { userRepositoryMockk.findById(any()) } answers { error("boom!") }

        runCatching { avstemmingController.get(id = UUID.randomUUID()) }
            .onSuccess { fail("Kjøring skulle feilet!") }
            .onFailure {
                val kodelinjer = it.finnFeiledeLinjer()

                assertThat(kodelinjer).all {
                    containsSubstring("intern: ExceptionHandlerTest.kt (linje:")
                    containsSubstring("intern: com.github.jactor.rises.persistence.user.UserRepository")
                }
            }
    }

    class TestController {
        fun illegalArgumentException() = IllegalArgumentException("feil input!!!")
    }
}
