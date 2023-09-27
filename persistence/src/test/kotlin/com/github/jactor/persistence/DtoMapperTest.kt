package com.github.jactor.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import com.github.jactor.shared.dto.UserDto
import com.github.jactor.shared.dto.UserType

internal class DtoMapperTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should map a user to json`() {
        val userlDto = UserDto()
        userlDto.id = 1L
        userlDto.emailAddress = "some@where"
        userlDto.username = "mine"
        userlDto.userType = UserType.ACTIVE

        assertAll(
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("id").contains("\"id\":1") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("email address").contains("\"emailAddress\":\"some@where\"") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("username").contains("\"username\":\"mine\"") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("user type").contains("\"userType\":\"ACTIVE\"") }
        )
    }
}