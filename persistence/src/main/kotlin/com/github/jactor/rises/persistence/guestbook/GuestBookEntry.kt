package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.util.toPersistent
import com.github.jactor.rises.shared.api.GuestBookEntryDto
import java.util.UUID

@JvmRecord
data class GuestBookEntry(
    val entry: String,
    val guestName: String,
    val guestBookId: UUID?,
    val persistent: Persistent,
) {
    val id: UUID? get() = persistent.id

    constructor(guestBookEntryDto: GuestBookEntryDto) : this(
        entry = requireNotNull(guestBookEntryDto.entry) { "Entry cannot be null!" },
        guestName = requireNotNull(guestBookEntryDto.creatorName) { "Creator name cannot be null!" },
        persistent = guestBookEntryDto.persistentDto.toPersistent(),
        guestBookId = guestBookEntryDto.guestBookId,
    )

    fun toGuestBookEntryDto() = GuestBookEntryDto(
        entry = entry,
        creatorName = guestName,
        guestBookId = guestBookId,
        persistentDto = persistent.toPersistentDto(),
    )

    fun toGuestBookEntryDao() = GuestBookEntryDao(
        id = id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        guestName = guestName,
        entry = entry,
        guestBookId = guestBookId,
    )
}
