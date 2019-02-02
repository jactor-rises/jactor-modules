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
            persistent: PersistentDto, created: LocalDate?, title: String?, user: UserDto?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            created, title, user
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }

}