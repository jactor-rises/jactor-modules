package com.github.jactor.persistence.dto

import java.time.LocalDateTime

data class PersistentDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null
)

interface Persistent {
    fun fetchPersistentDto(): PersistentDto
    fun getId(): Long?
    fun setId(id: Long?)

    fun setPersistentId(id: Long?) : PersistentDto {
        val persistentDto = PersistentDto()

        if (id != null && id > 0) {
            persistentDto.id = id
        }

        return persistentDto
    }
}
