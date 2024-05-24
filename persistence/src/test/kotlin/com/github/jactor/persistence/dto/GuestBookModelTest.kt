package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookModelTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookModel = GuestBookModel()
        guestBookModel.entries = setOf(GuestBookEntryModel())
        guestBookModel.title = "title"
        guestBookModel.userInternal = UserModel()

        val (_, entries, title, userInternal) = GuestBookModel(guestBookModel.persistentModel, guestBookModel)

        assertAll {
            assertThat(entries).isEqualTo(guestBookModel.entries)
            assertThat(title).isEqualTo(guestBookModel.title)
            assertThat(userInternal).isEqualTo(guestBookModel.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookModel(
            persistentModel, GuestBookModel()
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
