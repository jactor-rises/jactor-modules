package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.dto.PersistentDto
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

    internal constructor(persistentDto: PersistentDto) {
        createdBy = persistentDto.createdBy
        timeOfCreation = persistentDto.timeOfCreation
        modifiedBy = persistentDto.modifiedBy
        timeOfModification = persistentDto.timeOfModification
    }

    fun modifiedBy(modifier: String) {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()
    }

    fun asPersistentDto(id: UUID?): PersistentDto {
        return PersistentDto(id, createdBy, timeOfCreation, modifiedBy, timeOfModification)
    }
}
