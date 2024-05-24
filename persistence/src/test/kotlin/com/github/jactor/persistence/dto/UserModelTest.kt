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

        val (_, person, emailAddress, username) = UserModel(userModel.persistentModel, userModel)

        assertAll {
            assertThat(emailAddress).isEqualTo(userModel.emailAddress)
            assertThat(person).isEqualTo(userModel.person)
            assertThat(username).isEqualTo(userModel.username)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentModel = PersistentModel()
        persistentModel.createdBy = "jactor"
        persistentModel.timeOfCreation = LocalDateTime.now()
        persistentModel.id = UUID.randomUUID()
        persistentModel.modifiedBy = "tip"
        persistentModel.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = UserModel(
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
