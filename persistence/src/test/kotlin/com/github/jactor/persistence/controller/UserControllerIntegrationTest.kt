package com.github.jactor.persistence.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.command.CreateUserCommand
import com.github.jactor.persistence.command.CreateUserCommandResponse
import com.github.jactor.persistence.entity.UniqueUsername

internal class UserControllerIntegrationTest: AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(UniqueUsername.generate(name = "turbo"), surname =  "Someone")

        val response = testRestTemplate.postForEntity(
            "${basePath()}/user", HttpEntity(createUserCommand), CreateUserCommandResponse::class.java
        )

        assertAll(
            { assertThat(response.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(response.body?.userInternal).`as`("response.user").isNotNull() },
            { assertThat(response.body?.userInternal?.id).`as`("userDto.id").isNotNull() }
        )
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

        assertAll(
            { assertThat(response.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(response.body?.userInternal).`as`("response.user").isNotNull() },
            { assertThat(response.body?.userInternal?.emailAddress).`as`("userDto.emailAddress").isEqualTo("somewhere@somehow.com") }
        )
    }

    private fun basePath(): String {
        return "http://localhost:$port/jactor-persistence"
    }
}
