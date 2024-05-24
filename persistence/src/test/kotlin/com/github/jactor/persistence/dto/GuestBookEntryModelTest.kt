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

        val (_, guestBook, creatorName, entry) = GuestBookEntryModel(guestBookEntryModel.persistentDto, guestBookEntryModel)

        assertAll {
            assertThat(creatorName).isEqualTo(guestBookEntryModel.creatorName)
            assertThat(guestBook).isEqualTo(guestBookEntryModel.guestBook)
            assertThat(entry).isEqualTo(guestBookEntryModel.entry)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookEntryModel(
            persistentDto,
            GuestBookEntryModel()
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
