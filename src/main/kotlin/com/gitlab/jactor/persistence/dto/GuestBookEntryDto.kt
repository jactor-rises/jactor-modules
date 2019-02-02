package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class GuestBookEntryDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var guestBook: GuestBookDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : AsPersistentDto {
    constructor(
            guestBookEntry: GuestBookEntryDto
    ) : this(
            guestBookEntry.id, guestBookEntry.createdBy, guestBookEntry.creationTime, guestBookEntry.updatedBy, guestBookEntry.updatedTime,
            guestBookEntry.guestBook, guestBookEntry.creatorName, guestBookEntry.entry
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
