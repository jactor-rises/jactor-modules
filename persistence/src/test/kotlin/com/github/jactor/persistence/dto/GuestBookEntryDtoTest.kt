package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookEntryDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.entry = "entry"

        val (_, guestBook, creatorName, entry) = GuestBookEntryDto(guestBookEntryDto.persistentDto, guestBookEntryDto)

        assertAll {
            assertThat(creatorName).isEqualTo(guestBookEntryDto.creatorName)
            assertThat(guestBook).isEqualTo(guestBookEntryDto.guestBook)
            assertThat(entry).isEqualTo(guestBookEntryDto.entry)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookEntryDto(
            persistentDto,
            GuestBookEntryDto()
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
