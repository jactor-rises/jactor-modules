package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.GuestBookModel
import com.github.jactor.persistence.dto.GuestBookEntryModel

internal object GuestBookBuilder {
    fun new(guestBookModel: GuestBookModel = GuestBookModel()): GuestBookData = GuestBookData(
        guestBookModel = guestBookModel.copy(
            persistentDto = guestBookModel.persistentDto.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(guestBookModel: GuestBookModel): GuestBookData = GuestBookData(
        guestBookModel = guestBookModel
    )

    @JvmRecord
    data class GuestBookData(val guestBookModel: GuestBookModel, val guestBookEntryModel: GuestBookEntryModel? = null) {
        fun withEntry(guestBookEntryModel: GuestBookEntryModel): GuestBookData = copy(
            guestBookEntryModel = guestBookEntryModel.copy(guestBookEntryModel.persistentDto.copy(id = UUID.randomUUID()))
        )

        fun withEntryContainingPersistentId(guestBookEntryModel: GuestBookEntryModel): GuestBookData = copy(
            guestBookEntryModel = guestBookEntryModel
        )

        fun buildGuestBookEntity(): GuestBookEntity = GuestBookEntity(guestBook = guestBookModel)
        fun buildGuestBookEntryEntity(): GuestBookEntryEntity = GuestBookEntryEntity(
            guestBookEntry = guestBookEntryModel ?: error("no guest book entry provided!")
        )

    }
}
