package com.github.jactor.persistence.guestbook

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.shared.api.GuestBookEntryDto

@JvmRecord
data class GuestBookEntryModel(
    val creatorName: String? = null,
    val entry: String? = null,
    val guestBook: GuestBookModel? = null,
    val persistentModel: PersistentModel = PersistentModel(),
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("No creator is provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("No entry is provided!")

    constructor(
        persistentModel: PersistentModel, guestBookEntry: GuestBookEntryModel
    ) : this(
        persistentModel = persistentModel,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )

    constructor(guestBookEntryDto: GuestBookEntryDto) : this(
        persistentModel = PersistentModel(guestBookEntryDto.persistentDto),
        guestBook = guestBookEntryDto.guestBook?.let { GuestBookModel(guestBookDto = it) }
    )

    fun toDto() = GuestBookEntryDto(
        entry = entry,
        creatorName = creatorName,
        guestBook = guestBook?.toDto(),
        persistentDto = persistentModel.toDto(),
    )
}
