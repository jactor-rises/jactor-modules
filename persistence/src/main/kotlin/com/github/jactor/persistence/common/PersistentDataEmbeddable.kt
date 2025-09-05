package com.github.jactor.persistence.common

import java.time.LocalDateTime
import java.util.UUID
import jakarta.persistence.Embeddable

@Embeddable
class PersistentDataEmbeddable {
    var createdBy: String
    var timeOfCreation: LocalDateTime
    var modifiedBy: String
    var timeOfModification: LocalDateTime

    constructor() {
        createdBy = "todo"
        timeOfCreation = LocalDateTime.now()
        modifiedBy = "todo"
        timeOfModification = LocalDateTime.now()
    }

    internal constructor(persistent: Persistent) {
        createdBy = persistent.createdBy
        timeOfCreation = persistent.timeOfCreation
        modifiedBy = persistent.modifiedBy
        timeOfModification = persistent.timeOfModification
    }

    fun modifiedBy(modifier: String) {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()
    }

    fun toModel(id: UUID?): Persistent {
        return Persistent(
            createdBy = createdBy,
            id = id,
            modifiedBy = modifiedBy,
            timeOfCreation = timeOfCreation,
            timeOfModification = timeOfModification
        )
    }
}
