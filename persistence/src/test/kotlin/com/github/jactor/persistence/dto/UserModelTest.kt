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
        val userModel = UserModel(
            emailAddress = "somewhere@time",
            person = PersonModel(),
            username = "me"
        )

        val (_, person, emailAddress, username) = UserModel(userModel.persistentModel, userModel)

        assertAll {
            assertThat(emailAddress).isEqualTo(userModel.emailAddress)
            assertThat(person).isEqualTo(userModel.person)
            assertThat(username).isEqualTo(userModel.username)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentModel = PersistentModel(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now()
        )

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = UserModel(
            persistentModel,
            UserModel()
        ).persistentModel

        assertAll {
            assertThat(createdBy).isEqualTo(persistentModel.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentModel.timeOfCreation)
            assertThat(id).isEqualTo(persistentModel.id)
            assertThat(modifiedBy).isEqualTo(persistentModel.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentModel.timeOfModification)
        }
    }
}
