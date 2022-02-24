package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class GuestBookEntryDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.entry = "entry"

        val (_, guestBook, creatorName, entry) = GuestBookEntryDto(guestBookEntryDto.persistentDto, guestBookEntryDto)

        assertAll(
            { assertThat(creatorName).`as`("creator name").isEqualTo(guestBookEntryDto.creatorName) },
            { assertThat(guestBook).`as`("guest book").isEqualTo(guestBookEntryDto.guestBook) },
            { assertThat(entry).`as`("entry").isEqualTo(guestBookEntryDto.entry) }
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookEntryDto(persistentDto, GuestBookEntryDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
