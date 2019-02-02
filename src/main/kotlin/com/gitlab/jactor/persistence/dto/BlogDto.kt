package com.gitlab.jactor.persistence.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class BlogDto(
        private var id: Long? = null,
        private var createdBy: String? = null,
        private var creationTime: LocalDateTime? = null,
        private var updatedBy: String? = null,
        private var updatedTime: LocalDateTime? = null,
        var created: LocalDate? = null,
        var title: String? = null,
        var user:UserDto ? = null
) : AsPersistentDto {
    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }

}