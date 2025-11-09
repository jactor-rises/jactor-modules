package com.github.jactor.rises.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.rises.shared.api.PersistentDto
import com.github.jactor.rises.shared.api.UserDto
import com.github.jactor.rises.shared.api.UserType
import java.util.UUID
import org.junit.jupiter.api.Test

internal class DtoMapperTest {
    private val objectMapper: ObjectMapper = JactorPersistenceConfig().objectMapper()

    @Test
    fun `should map a user to json`() {
        val uuid = UUID.randomUUID()
        val userDto = UserDto(
            persistentDto = PersistentDto(id = uuid),
            emailAddress = "some@where",
            username = "mine",
            userType = UserType.ACTIVE
        )

        val json = objectMapper.writeValueAsString(userDto)

        assertAll {
            assertThat(actual = json, name = "id").contains(""""id":"$uuid"""")
            assertThat(actual = json, name = "emailAddress").contains(""""emailAddress":"some@where"""")
            assertThat(actual = json, name = "username").contains(""""username":"mine"""")
            assertThat(actual = json, name = "userType").contains(""""userType":"ACTIVE"""")
        }
    }
}
