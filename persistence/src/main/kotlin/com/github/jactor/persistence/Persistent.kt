package com.github.jactor.persistence

import com.github.jactor.shared.api.PersistentDto
import java.time.LocalDateTime
import java.util.UUID

@JvmRecord
data class Persistent(
    val id: UUID? = null,

    val createdBy: String = "todo: #3",
    val modifiedBy: String = "todo: #3",
    val timeOfCreation: LocalDateTime = LocalDateTime.now(),
    val timeOfModification: LocalDateTime = LocalDateTime.now()
) {
    fun toPersistentDto() = PersistentDto(
        id = id,

        createdBy = createdBy,
        modifiedBy = modifiedBy,
        timeOfCreation = timeOfCreation,
        timeOfModification = timeOfModification,
    )
}
