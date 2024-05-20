package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID

data class PersistentDto(
        var id: UUID? = null,
        var createdBy: String = "todo: #3",
        var timeOfCreation: LocalDateTime = LocalDateTime.now(),
        var modifiedBy: String = "todo: #3",
        var timeOfModification: LocalDateTime = LocalDateTime.now()
)