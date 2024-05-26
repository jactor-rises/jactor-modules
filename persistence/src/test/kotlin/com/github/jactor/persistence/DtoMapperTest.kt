package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains

internal class DtoMapperTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should map a user to json`() {
        val uuid = UUID.randomUUID()
        val userlDto = UserDto(
            persistentDto = PersistentDto(id = uuid),
            emailAddress = "some@where",
            username = "mine",
            userType = UserType.ACTIVE
        )

        assertAll {
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"id\":\"$uuid\"")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"emailAddress\":\"some@where\"")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"username\":\"mine\"")
            assertThat(objectMapper.writeValueAsString(userlDto)).contains("\"userType\":\"ACTIVE\"")
        }
    }
}