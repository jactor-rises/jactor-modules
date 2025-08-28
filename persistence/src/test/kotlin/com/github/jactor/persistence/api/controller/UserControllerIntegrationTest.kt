package com.github.jactor.persistence.api.controller

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.timestamped
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

@Disabled("wip")
internal class UserControllerIntegrationTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(timestamped(username = "turbo"), surname = "Someone")

        val response = testRestTemplate.postForEntity(
            "${basePath()}/user", HttpEntity(createUserCommand), UserDto::class.java
        )

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(response.body).isNotNull()
            assertThat(response.body?.persistentDto?.id).isNotNull()
        }
    }

    @Test
    fun `should create a new user with an email address`() {
        val createUserCommand = CreateUserCommand(
            timestamped(username = "turbo"),
            surname = "Someone",
            emailAddress = "somewhere@somehow.com"
        )

        val response = testRestTemplate.postForEntity(
            "${basePath()}/user", HttpEntity(createUserCommand), UserDto::class.java
        )

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(response.body).isNotNull()
            assertThat(response.body?.emailAddress).isEqualTo("somewhere@somehow.com")
        }
    }

    private fun basePath(): String {
        return "http://localhost:$port/jactor-persistence"
    }
}
