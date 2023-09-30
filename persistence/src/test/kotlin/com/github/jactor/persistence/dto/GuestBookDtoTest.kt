package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookDto = GuestBookDto()
        guestBookDto.entries = setOf(GuestBookEntryDto())
        guestBookDto.title = "title"
        guestBookDto.userInternal = UserInternalDto()

        val (_, entries, title, userInternal) = GuestBookDto(guestBookDto.persistentDto, guestBookDto)

        assertAll {
            assertThat(entries).isEqualTo(guestBookDto.entries)
            assertThat(title).isEqualTo(guestBookDto.title)
            assertThat(userInternal).isEqualTo(guestBookDto.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookDto(
            persistentDto, GuestBookDto()
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
