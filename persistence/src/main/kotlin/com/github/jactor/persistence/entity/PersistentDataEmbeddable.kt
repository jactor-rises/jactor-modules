package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.dto.PersistentModel
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

    internal constructor(persistentModel: PersistentModel) {
        createdBy = persistentModel.createdBy
        timeOfCreation = persistentModel.timeOfCreation
        modifiedBy = persistentModel.modifiedBy
        timeOfModification = persistentModel.timeOfModification
    }

    fun modifiedBy(modifier: String) {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()
    }

    fun toModel(id: UUID?): PersistentModel {
        return PersistentModel(
            createdBy = createdBy,
            id = id,
            modifiedBy = modifiedBy,
            timeOfCreation = timeOfCreation,
            timeOfModification = timeOfModification
        )
    }
}
