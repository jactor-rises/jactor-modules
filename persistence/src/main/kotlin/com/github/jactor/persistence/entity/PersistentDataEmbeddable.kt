package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.dto.PersistentModel
import jakarta.persistence.Embeddable

@Embeddable
class PersistentDataEmbeddable : PersistentData {
    override val createdBy: String
    override val timeOfCreation: LocalDateTime
    override var modifiedBy: String
        private set
    override var timeOfModification: LocalDateTime
        private set

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

    fun asPersistentDto(id: UUID?): PersistentModel {
        return PersistentModel(id, createdBy, timeOfCreation, modifiedBy, timeOfModification)
    }
}
