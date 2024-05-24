package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class UserModelTest {

    @Test
    fun `should have a copy constructor`() {
        val userModel = UserModel()
        userModel.emailAddress = "somewhere@time"
        userModel.person = PersonModel()
        userModel.username = "me"

        val (_, person, emailAddress, username) = UserModel(userModel.persistentDto, userModel)

        assertAll {
            assertThat(emailAddress).isEqualTo(userModel.emailAddress)
            assertThat(person).isEqualTo(userModel.person)
            assertThat(username).isEqualTo(userModel.username)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = UUID.randomUUID()
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = UserModel(
            persistentDto,
            UserModel()
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
