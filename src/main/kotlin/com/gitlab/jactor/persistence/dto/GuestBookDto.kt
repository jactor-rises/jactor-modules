package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class GuestBookDto(
        private var id: Long? = null,
        private var createdBy: String? = null,
        private var creationTime: LocalDateTime? = null,
        private var updatedBy: String? = null,
        private var updatedTime: LocalDateTime? = null,
        var entries: Set<GuestBookEntryDto>? = null,
        var title: String? = null,
        var user: UserDto? = null
) : AsPersistentDto {
    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
