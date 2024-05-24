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

        val (_, entries, title, userInternal) = GuestBookModel(guestBookModel.persistentDto, guestBookModel)

        assertAll {
            assertThat(entries).isEqualTo(guestBookModel.entries)
            assertThat(title).isEqualTo(guestBookModel.title)
            assertThat(userInternal).isEqualTo(guestBookModel.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookModel(
            persistentDto, GuestBookModel()
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
