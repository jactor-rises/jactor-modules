package com.github.jactor.persistence.dto

import java.time.LocalDateTime

data class PersistentDto(
        var id: Long? = null,
        var createdBy: String = "todo: #3",
        var creationTime: LocalDateTime = LocalDateTime.now(),
        var updatedBy: String = "todo: #3",
        var updatedTime: LocalDateTime = LocalDateTime.now()
)

interface Persistent {
    fun fetchPersistentDto(): PersistentDto
    fun getId(): Long?
    fun setId(id: Long?)

    fun setPersistentId(id: Long?): PersistentDto {
        val persistentDto = PersistentDto()

        if (id != null && id > 0) {
            persistentDto.id = id
        }

        return persistentDto
    }
}
