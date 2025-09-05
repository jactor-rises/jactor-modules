package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.timestamped
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.test.all
import com.github.jactor.shared.test.contains
import com.github.jactor.shared.test.equals
import com.github.jactor.shared.test.named
import assertk.assertThat
import assertk.fail

internal class UserControllerIntegrationTest @Autowired constructor(
    private val webTestClient: WebTestClient
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(username = timestamped(username = "turbo"), surname = "Someone")

        webTestClient.post()
            .uri("/user")
            .bodyValue(createUserCommand)
            .exchange()
            .expectStatus().isCreated
    }

    @Test
    fun `should create a new user with an email address`() {
        val createUserCommand = CreateUserCommand(
            username = timestamped("turbo"),
            surname = "Someone",
            emailAddress = "somewhere@somehow.com"
        )

        val userDto = webTestClient.post()
            .uri("/user")
            .bodyValue(createUserCommand)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDto::class.java)
            .returnResult().responseBody ?: fail(message = "no user created")

        assertThat(userDto).all {
            emailAddress named "email address" equals "somewhere@somehow.com"
            person?.surname named "surname" equals "Someone"
            username named "username" contains "turbo"
        }
    }
}
