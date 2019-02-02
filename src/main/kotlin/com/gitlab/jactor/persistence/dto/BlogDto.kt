package com.gitlab.jactor.persistence.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class BlogDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var created: LocalDate? = null,
        var title: String? = null,
        var user: UserDto? = null
) : AsPersistentDto {
    constructor(
            blog: BlogDto
    ) : this(
            blog.id, blog.createdBy, blog.creationTime, blog.updatedBy, blog.updatedTime,
            blog.created, blog.title, blog.user
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }

}