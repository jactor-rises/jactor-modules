package com.github.jactor.persistence.common

import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.shared.api.PersistentDto

@JvmRecord
data class Persistent(
    val createdBy: String = "todo: #3",
    val id: UUID? = null,
    val modifiedBy: String = "todo: #3",
    val timeOfCreation: LocalDateTime = LocalDateTime.now(),
    val timeOfModification: LocalDateTime = LocalDateTime.now()
) {
    fun toDto() = PersistentDto(
        createdBy = createdBy,
        id = id,
        modifiedBy = modifiedBy,
        timeOfCreation = timeOfCreation,
        timeOfModification = timeOfModification,
    )

    fun toEmbeddable() = PersistentDataEmbeddable(persistent = this)

    constructor(persistentDto: PersistentDto) : this(
        createdBy = persistentDto.createdBy ?: "",
        id = persistentDto.id,
        modifiedBy = persistentDto.modifiedBy ?: "",
        timeOfCreation = persistentDto.timeOfCreation ?: LocalDateTime.now(),
        timeOfModification = persistentDto.timeOfModification ?: LocalDateTime.now()
    )
}
