package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.time.Now.Companion.asDateTime
import javax.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
class PersistentDataEmbeddable : PersistentData {
    override var createdBy: String
        private set
    override var timeOfCreation: LocalDateTime
        private set
    override var modifiedBy: String
        private set
    override var timeOfModification: LocalDateTime
        private set

    protected constructor() {
        createdBy = "todo"
        timeOfCreation = asDateTime()
        modifiedBy = "todo"
        timeOfModification = asDateTime()
    }

    internal constructor(persistentDto: PersistentDto) {
        createdBy = persistentDto.createdBy
        timeOfCreation = persistentDto.timeOfCreation
        modifiedBy = persistentDto.modifiedBy
        timeOfModification = persistentDto.timeOfModification
    }

    fun modifiedBy(modifier: String) {
        modifiedBy = modifier
        timeOfModification = asDateTime()
    }

    fun asPersistentDto(id: Long?): PersistentDto {
        return PersistentDto(id, createdBy, timeOfCreation, modifiedBy, timeOfModification)
    }
}
