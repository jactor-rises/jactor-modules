package com.github.jactor.persistence

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initUserEntity
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import com.ninjasquad.springmockk.MockkBean
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every

@WebFluxTest(UserController::class)
@Import(UserService::class)
internal class UserControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @Suppress("unused") @MockkBean private val personServiceMockk: PersonService,
    @MockkBean private val userRepositoryMockk: UserRepository,
) {

    @Test
    fun `should not find a user by username`() {
        every { userRepositoryMockk.findByUsername("me") } returns Optional.empty()

        webTestClient.get()
            .uri("/user/name/me")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should find a user by username`() {
        every { userRepositoryMockk.findByUsername("me") } returns Optional.of(UserEntity(User()))

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
        every { userRepositoryMockk.findById(uuid) } returns Optional.empty()

        webTestClient.get()
            .uri("/user/$uuid")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should find a user by id`() {
        val uuid = UUID.randomUUID()
        every { userRepositoryMockk.findById(uuid) } returns Optional
            .of(initUserEntity(id = uuid))

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
        val user = User(persistent = Persistent(id = uuid))

        every { userRepositoryMockk.findById(uuid) } returns Optional.of(UserEntity(user = user))

        val userDto = webTestClient.put()
            .uri("/user/update")
            .bodyValue(user.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(UserDto::class.java)
            .returnResult().responseBody

        assertThat(userDto).isNotNull()
    }

    @Test
    fun `should find all usernames of active users`() {
        val bartDto = UserDto(person = PersonDto(address = AddressDto()), username = "bart", userType = UserType.ACTIVE)
        val lisaDto = UserDto(person = PersonDto(address = AddressDto()), username = "lisa", userType = UserType.ACTIVE)
        val bart = UserEntity(User(bartDto))
        val lisa = UserEntity(User(lisaDto))

        every { userRepositoryMockk.findByUserTypeIn(listOf(UserEntity.UserType.ACTIVE)) } returns listOf(bart, lisa)

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
        val uuid = UUID.randomUUID()
        every { userRepositoryMockk.findById(uuid) } returns Optional.of(
            UserEntity(User(persistent = Persistent(id = uuid)))
        )

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
        every { userRepositoryMockk.findByUsername("turbo") } returns Optional.of(UserEntity())

        webTestClient.post()
            .uri("/user")
            .bodyValue(CreateUserCommand(username = "turbo"))
            .exchange()
            .expectStatus().isBadRequest
    }
}
