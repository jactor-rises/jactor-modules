package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import com.github.jactor.shared.finnFeiledeLinjer
import com.github.jactor.shared.test.containsSubstring
import assertk.all
import assertk.assertThat
import assertk.assertions.isTrue
import assertk.fail
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

internal class ExceptionHandlerTest {
    private val exceptionHandler = ExceptionHandler()

    @Test
    fun `skal være internal server error når IllegalArgumentException ikke oppstår i Controller`() {
        val response = exceptionHandler.handleBadRequest(e = TestController().initFromService()).block()

        assertThat(response!!.statusCode.isSameCodeAs(HttpStatus.INTERNAL_SERVER_ERROR)).isTrue()
    }

    @Test
    fun `skal være bad request når IllegalArgumentException oppstår i Controller`() {
        val response = exceptionHandler.handleBadRequest(e = TestController().initFromController()).block()

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
                    containsSubstring("intern: user.kt (linje:")
                    containsSubstring("intern: com.github.jactor.persistence.UserRepository")
                }
            }
    }

    class TestController {
        fun initFromController() = IllegalArgumentException("feil input!!!")
        fun initFromService(): IllegalArgumentException = TestService().init()
    }

    class TestService {
        fun init(): IllegalArgumentException = IllegalArgumentException("feil input!!!")
    }
}
