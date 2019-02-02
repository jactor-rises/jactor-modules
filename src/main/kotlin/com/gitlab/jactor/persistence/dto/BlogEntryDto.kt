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
            blogEntry: BlogEntryDto
    ) : this(
            blogEntry.id, blogEntry.createdBy, blogEntry.creationTime, blogEntry.updatedBy, blogEntry.updatedTime,
            blogEntry.blog, blogEntry.creatorName, blogEntry.entry
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
