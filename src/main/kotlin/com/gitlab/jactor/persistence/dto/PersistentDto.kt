package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class PersistentDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null
)

interface AsPersistentDto {
    fun asPersistentDto(): PersistentDto
}
