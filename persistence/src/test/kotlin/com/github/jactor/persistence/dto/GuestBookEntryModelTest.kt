package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookEntryModelTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookEntryModel = GuestBookEntryModel()
        guestBookEntryModel.creatorName = "me"
        guestBookEntryModel.guestBook = GuestBookModel()
        guestBookEntryModel.entry = "entry"

        val (_, guestBook, creatorName, entry) = GuestBookEntryModel(guestBookEntryModel.persistentModel, guestBookEntryModel)

        assertAll {
            assertThat(creatorName).isEqualTo(guestBookEntryModel.creatorName)
            assertThat(guestBook).isEqualTo(guestBookEntryModel.guestBook)
            assertThat(entry).isEqualTo(guestBookEntryModel.entry)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookEntryModel(
            persistentModel,
            GuestBookEntryModel()
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
