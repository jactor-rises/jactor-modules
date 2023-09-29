package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import com.github.jactor.shared.dto.UserDto
import com.github.jactor.shared.dto.UserType
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains

internal class DtoMapperTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should map a user to json`() {
        val userlDto = UserDto()
        userlDto.id = 1L
        userlDto.emailAddress = "some@where"
        userlDto.username = "mine"
        userlDto.userType = UserType.ACTIVE

        assertAll {
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"id\":1")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"emailAddress\":\"some@where\"")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"username\":\"mine\"")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"userType\":\"ACTIVE\"")
        }
    }
}