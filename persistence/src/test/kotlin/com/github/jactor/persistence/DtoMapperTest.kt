package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains

internal class DtoMapperTest @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should map a user to json`() {
        val uuid = UUID.randomUUID()
        val userlDto = UserDto(
            persistentDto = PersistentDto(id = uuid),
            emailAddress = "some@where",
            username = "mine",
            userType = UserType.ACTIVE
        )

        val json = objectMapper.writeValueAsString(userlDto)

        assertAll {
            assertThat(actual = json, name = "id").contains(""""id":"$uuid"""")
            assertThat(actual = json, name = "emailAddress").contains(""""emailAddress":"some@where"""")
            assertThat(actual = json, name = "username").contains(""""username":"mine"""")
            assertThat(actual = json, name = "userType").contains(""""userType":"ACTIVE"""")
        }
    }
}
