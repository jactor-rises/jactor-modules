package com.github.jactor.persistence

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject

@WebFluxTest(UserController::class)
@Import(UserService::class)
internal class UserControllerTest @Autowired constructor(private val webTestClient: WebTestClient) {
    @BeforeEach
    fun `mock UserRepository`() = mockkObject(UserRepositoryObject)

    @AfterEach
    fun `unmock UserRepository`() = unmockkObject(UserRepositoryObject)

    @Test
    fun `should not find a user by username`() {
        every { UserRepositoryObject.findByUsername(username = "me") } returns null

        webTestClient.get()
            .uri("/user/name/me")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should find a user by username`() {
        every { UserRepositoryObject.findByUsername(username = "me") } returns initUser().toUserDao()

        val userDto = webTestClient.get()
            .uri("/user/name/me")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
            .returnResult().responseBody

        assertThat(userDto).isNotNull()
    }

    @Test
    fun `should not get a user by id`() {
        val uuid = UUID.randomUUID()
        every { UserRepositoryObject.findById(id = uuid) } returns null

        webTestClient.get()
            .uri("/user/$uuid")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should find a user by id`() {
        val uuid = UUID.randomUUID()
        every { UserRepositoryObject.findById(uuid) } returns initUserDao(id = uuid)

        val userDto = webTestClient.get()
            .uri("/user/$uuid")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
            .returnResult().responseBody

        assertThat(userDto).isNotNull()
    }

    @Test
    fun `should modify existing user`() {
        val uuid = UUID.randomUUID()
        val user = initUser(persistent = Persistent(id = uuid))

        every { UserRepositoryObject.findById(uuid) } returns user.toUserDao()

        val userDto = webTestClient.put()
            .uri("/user/update")
            .bodyValue(user.toUserDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(UserDto::class.java)
            .returnResult().responseBody

        assertThat(userDto).isNotNull()
    }

    @Test
    fun `should find all usernames of active users`() {
        every {
            UserRepositoryObject.findUsernames(userType = listOf(UserDao.UserType.ACTIVE))
        } returns listOf("bart", "lisa")

        val usernames = webTestClient.get()
            .uri("/user/usernames")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody

        assertThat(usernames).isEqualTo("""["bart","lisa"]""")
    }

    @Test
    fun `should accept if user id is not null`() {
        val uuid = UUID.randomUUID().also {
            every { UserRepositoryObject.findById(id = it) } returns initUser(
                persistent = Persistent(id = it),
            ).toUserDao()
        }

        webTestClient.put()
            .uri("/user/update")
            .bodyValue(UserDto(persistentDto = PersistentDto(id = uuid)))
            .exchange()
            .expectStatus().isAccepted
    }

    @Test
    fun `should not accept if user id is null`() {
        webTestClient.put()
            .uri("/user/update")
            .bodyValue(UserDto(persistentDto = PersistentDto(id = null)))
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should return BAD_REQUEST when username is occupied`() {
        every { UserRepositoryObject.findByUsername(username = "turbo") } returns initUser().toUserDao()

        webTestClient.post()
            .uri("/user")
            .bodyValue(CreateUserCommand(username = "turbo"))
            .exchange()
            .expectStatus().isBadRequest
    }
}
