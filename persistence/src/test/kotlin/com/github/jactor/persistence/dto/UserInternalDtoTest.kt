package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class UserInternalDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val userInternalDto = UserInternalDto()
        userInternalDto.emailAddress = "somewhere@time"
        userInternalDto.person = PersonInternalDto()
        userInternalDto.username = "me"

        val (_, person, emailAddress, username) = UserInternalDto(userInternalDto.persistentDto, userInternalDto)

        assertAll(
            { assertThat(emailAddress).`as`("email address").isEqualTo(userInternalDto.emailAddress) },
            { assertThat(person).`as`("person").isEqualTo(userInternalDto.person) },
            { assertThat(username).`as`("user name").isEqualTo(userInternalDto.username) }
        )
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = 1L
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = UserInternalDto(persistentDto, UserInternalDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
