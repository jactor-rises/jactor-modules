package com.github.jactor.persistence.controller

import com.github.jactor.persistence.command.CreateUserCommand
import com.github.jactor.persistence.command.CreateUserCommandResponse
import com.github.jactor.persistence.entity.UniqueUsername
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
internal class UserControllerIntegrationTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @LocalServerPort
    private val port = 0

    @Test
    fun `should create a new user`() {
        val createUserCommand = CreateUserCommand(UniqueUsername.generate("turbo"), "Someone")

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
        val createUserCommand = CreateUserCommand(UniqueUsername.generate("turbo"), "Someone")
        createUserCommand.emailAddress = "somewhere@somehow.com"

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
