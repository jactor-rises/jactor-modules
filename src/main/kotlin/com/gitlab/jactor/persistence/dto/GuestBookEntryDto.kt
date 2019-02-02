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
            persistent: PersistentDto,
            guestBook: GuestBookDto?, creatorName: String?, entry: String?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            guestBook, creatorName, entry
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
