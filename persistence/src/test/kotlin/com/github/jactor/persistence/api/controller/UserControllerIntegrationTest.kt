package com.github.jactor.persistence.api.controller

import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.api.command.CreateUserCommand
import com.github.jactor.persistence.api.command.CreateUserCommandResponse
import com.github.jactor.persistence.entity.UniqueUsername
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class UserControllerIntegrationTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(UniqueUsername.generate(name = "turbo"), surname = "Someone")

        val response = testRestTemplate.postForEntity(
            "${basePath()}/user", HttpEntity(createUserCommand), CreateUserCommandResponse::class.java
        )

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(response.body?.userInternal).isNotNull()
            assertThat(response.body?.userInternal?.id).isNotNull()
        }
    }

    @Test
    fun `should create a new user with an email address`() {
        val createUserCommand = CreateUserCommand(
            UniqueUsername.generate("turbo"),
            surname = "Someone",
            emailAddress = "somewhere@somehow.com"
        )

        val response = testRestTemplate.postForEntity(
            "${basePath()}/user", HttpEntity(createUserCommand), CreateUserCommandResponse::class.java
        )

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(response.body?.userInternal).isNotNull()
            assertThat(response.body?.userInternal?.emailAddress).isEqualTo("somewhere@somehow.com")
        }
    }

    private fun basePath(): String {
        return "http://localhost:$port/jactor-persistence"
    }
}
