package com.github.jactor.persistence.api.exception

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.ExceptionHandler
import com.github.jactor.persistence.api.controller.UserController
import com.github.jactor.persistence.test.containsSubstring
import com.github.jactor.persistence.user.UserRepository
import com.github.jactor.persistence.user.UserService
import com.github.jactor.shared.finnFeiledeLinjer
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isTrue
import assertk.fail
import io.mockk.every
import io.mockk.mockk

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

    class TestController {
        fun initFromController() = IllegalArgumentException("feil input!!!")
        fun initFromService(): IllegalArgumentException = TestService().init()
    }

    class TestService {
        fun init(): IllegalArgumentException = IllegalArgumentException("feil input!!!")
    }
}
