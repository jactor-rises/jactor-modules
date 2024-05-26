package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.shared.api.GuestBookDto

@JvmRecord
data class GuestBookModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val entries: Set<GuestBookEntryModel> = emptySet(),
    val title: String? = null,
    val user: UserModel? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(persistentModel: PersistentModel, guestBook: GuestBookModel) : this(
        persistentModel = persistentModel,
        entries = guestBook.entries,
        title = guestBook.title,
        user = guestBook.user
    )

    constructor(guestBookDto: GuestBookDto) : this(
        persistentModel = PersistentModel(guestBookDto.persistentDto),
        entries = guestBookDto.entries.map { GuestBookEntryModel(it) }.toSet(),
        title = guestBookDto.title,
        user = guestBookDto.userDto?.let { UserModel(userDto = it) }
    )

    fun toDto(): GuestBookDto = GuestBookDto(
        persistentDto = persistentModel.toDto(),
        entries = entries.map { entry: GuestBookEntryModel -> entry.toDto() }.toSet(),
        title = title,
        userDto = user?.toDto()
    )
}
