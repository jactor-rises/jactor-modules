package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class GuestBookDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var entries: Set<GuestBookEntryDto> = emptySet(),
        var title: String? = null,
        var user: UserDto? = null
) : AsPersistentDto {
    constructor(
            persistent: PersistentDto,
            entries: Set<GuestBookEntryDto>, title: String?, user: UserDto? = null
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            entries,  title, user
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
