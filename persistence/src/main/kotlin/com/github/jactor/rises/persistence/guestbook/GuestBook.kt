package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.util.toPersistent
import com.github.jactor.rises.shared.api.GuestBookDto
import java.util.UUID

@JvmRecord
data class GuestBook(
    val persistent: Persistent,
    val title: String?,
    val userId: UUID?,
) {
    val id: UUID get() = persistent.id ?: error("Guest book is not persisted!")

    constructor(persistent: Persistent, guestBook: GuestBook) : this(
        persistent = persistent,
        title = guestBook.title,
        userId = guestBook.userId,
    )

    constructor(guestBookDto: GuestBookDto) : this(
        persistent = guestBookDto.persistentDto.toPersistent(),
        title = guestBookDto.title,
        userId = guestBookDto.userId,
    )

    fun toDto(): GuestBookDto = GuestBookDto(
        persistentDto = persistent.toPersistentDto(),
        title = title,
        userId = userId,
    )

    fun toGuestBookDao() = GuestBookDao(
        id = persistent.id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        title = requireNotNull(title) { "Title cannot be null!" },
        userId = userId,
    )

    fun toGuestBookDto() = GuestBookDto(
        persistentDto = persistent.toPersistentDto(),
        title = title,
        userId = userId,
    )
}
