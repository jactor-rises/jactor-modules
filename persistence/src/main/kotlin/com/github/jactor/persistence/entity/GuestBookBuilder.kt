package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto

internal object GuestBookBuilder {
    fun new(guestBookDto: GuestBookDto = GuestBookDto()): GuestBookData = GuestBookData(
        guestBookDto = guestBookDto.copy(
            persistentDto = guestBookDto.persistentDto.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(guestBookDto: GuestBookDto): GuestBookData = GuestBookData(
        guestBookDto = guestBookDto
    )

    @JvmRecord
    data class GuestBookData(val guestBookDto: GuestBookDto, val guestBookEntryDto: GuestBookEntryDto? = null) {
        fun withEntry(guestBookEntryDto: GuestBookEntryDto): GuestBookData = copy(
            guestBookEntryDto = guestBookEntryDto.copy(guestBookEntryDto.persistentDto.copy(id = UUID.randomUUID()))
        )

        fun withEntryContainingPersistentId(guestBookEntryDto: GuestBookEntryDto): GuestBookData = copy(
            guestBookEntryDto = guestBookEntryDto
        )

        fun buildGuestBookEntity(): GuestBookEntity = GuestBookEntity(guestBook = guestBookDto)
        fun buildGuestBookEntryEntity(): GuestBookEntryEntity = GuestBookEntryEntity(
            guestBookEntry = guestBookEntryDto ?: error("no guest book entry provided!")
        )

    }
}
