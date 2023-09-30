package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class UserInternalDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val userInternalDto = UserInternalDto()
        userInternalDto.emailAddress = "somewhere@time"
        userInternalDto.person = PersonInternalDto()
        userInternalDto.username = "me"

        val (_, person, emailAddress, username) = UserInternalDto(userInternalDto.persistentDto, userInternalDto)

        assertAll {
            assertThat(emailAddress).isEqualTo(userInternalDto.emailAddress)
            assertThat(person).isEqualTo(userInternalDto.person)
            assertThat(username).isEqualTo(userInternalDto.username)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = 1L
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = UserInternalDto(
            persistentDto,
            UserInternalDto()
        ).persistentDto

        assertAll {
            assertThat(createdBy).isEqualTo(persistentDto.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentDto.timeOfCreation)
            assertThat(id).isEqualTo(persistentDto.id)
            assertThat(modifiedBy).isEqualTo(persistentDto.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentDto.timeOfModification)
        }
    }
}
