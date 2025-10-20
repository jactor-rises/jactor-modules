package com.github.jactor.persistence.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.fail
import com.github.jactor.persistence.Persistent
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.persistence.test.timestamped
import com.github.jactor.persistence.test.withId
import com.github.jactor.persistence.test.withPersistedData
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.test.all
import com.github.jactor.shared.test.contains
import com.github.jactor.shared.test.equals
import com.github.jactor.shared.test.named
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(UserController::class)
@Import(UserService::class, UserRepository::class)
internal class UserControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @MockkBean private val userRepositoryMockk: UserRepository,
) {

    @Test
    fun `should not find a user by username`() {
        every { userRepositoryMockk.findByUsername(username = "me") } returns null

        webTestClient.get()
            .uri("/user/name/me")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should find a user by username`() {
        every { userRepositoryMockk.findByUsername(username = "me") } returns initUser().toUserDao()

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
        every { userRepositoryMockk.findById(id = any()) } returns null

        webTestClient.get()
            .uri("/user/$uuid")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should find a user by id`() {
        val uuid = UUID.randomUUID()
        every { userRepositoryMockk.findById(id = any()) } returns initUserDao(id = uuid)

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

        every { userRepositoryMockk.findById(uuid) } returns user.toUserDao()
        every { userRepositoryMockk.save(any()) } answers { arg(0) }

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
            userRepositoryMockk.findUsernames(userType = listOf(UserDao.UserType.ACTIVE))
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
            every { userRepositoryMockk.save(any()) } answers { arg(0) }
            every { userRepositoryMockk.findById(id = it) } returns initUser(persistent = Persistent(id = it))
                .toUserDao()
        }

        webTestClient.put()
            .uri("/user/update")
            .bodyValue(UserDto().withPersistedData(id = uuid))
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
        every { userRepositoryMockk.contains(username = "turbo") } returns true

        webTestClient.post()
            .uri("/user")
            .bodyValue(CreateUserCommand(username = "turbo"))
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(username = timestamped(username = "turbo"), surname = "Someone")

        every { userRepositoryMockk.contains(any()) } returns false
        every { userRepositoryMockk.save(any()) } returns initUserDao(
            id = UUID.randomUUID(),
            username = createUserCommand.username,
        )

        webTestClient.post()
            .uri("/user")
            .bodyValue(createUserCommand)
            .exchange()
            .expectStatus().isCreated
    }

    @Test
    fun `should get a BAD_REQUEST if trying to create an existing user`() {
        val createUserCommand = CreateUserCommand(username = timestamped(username = "turbo"), surname = "Someone")

        every { userRepositoryMockk.contains(any()) } returns true

        webTestClient.post()
            .uri("/user")
            .bodyValue(createUserCommand)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should create a new user with an email address`() {
        val createUserCommand = CreateUserCommand(
            username = timestamped("turbo"),
            surname = "Someone",
            emailAddress = "somewhere@somehow.com"
        )

        coEvery { userRepositoryMockk.contains(any()) } returns false
        coEvery { userRepositoryMockk.save(any()) } returns initUser(
            username = createUserCommand.username,
            emailAddress = createUserCommand.emailAddress
        ).withId().toUserDao()

        val userDto = webTestClient.post()
            .uri("/user")
            .bodyValue(createUserCommand)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDto::class.java)
            .returnResult().responseBody ?: fail(message = "no user created")

        assertThat(userDto).all {
            emailAddress named "email address" equals "somewhere@somehow.com"
            username named "username" contains "turbo"
        }
    }
}
