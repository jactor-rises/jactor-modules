package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class BlogEntryDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var blog: BlogDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : AsPersistentDto {
    constructor(
            persistent: PersistentDto,
            blog: BlogDto?,
            creatorName: String?,
            entry: String?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            blog, creatorName, entry
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
