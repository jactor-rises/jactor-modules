package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class GuestBookDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookDto = GuestBookDto()
        guestBookDto.entries = setOf(GuestBookEntryDto())
        guestBookDto.title = "title"
        guestBookDto.userInternal = UserInternalDto()

        val (_, entries, title, userInternal) = GuestBookDto(guestBookDto.persistentDto, guestBookDto)

        assertAll(
            { assertThat(entries).`as`("entries").isEqualTo(guestBookDto.entries) },
            { assertThat(title).`as`("title").isEqualTo(guestBookDto.title) },
            { assertThat(userInternal).`as`("title").isEqualTo(guestBookDto.userInternal) }
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = GuestBookDto(persistentDto, GuestBookDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
