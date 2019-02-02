package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class GuestBookEntryDto(
        private var id: Long? = null,
        private var createdBy: String? = null,
        private var creationTime: LocalDateTime? = null,
        private var updatedBy: String? = null,
        private var updatedTime: LocalDateTime? = null,
        var guestBook: GuestBookDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : AsPersistentDto {
    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
