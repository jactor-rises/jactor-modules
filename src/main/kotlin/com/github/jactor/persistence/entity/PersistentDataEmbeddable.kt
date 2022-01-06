package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.time.Now
import javax.persistence.Embeddable
import java.time.LocalDateTime

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
        timeOfCreation = Now.asDateTime()
        modifiedBy = "todo"
        timeOfModification = Now.asDateTime()
    }

    internal constructor(persistentDto: PersistentDto) {
        createdBy = persistentDto.createdBy
        timeOfCreation = persistentDto.timeOfCreation
        modifiedBy = persistentDto.modifiedBy
        timeOfModification = persistentDto.timeOfModification
    }

    fun modifiedBy(modifier: String) {
        modifiedBy = modifier
        timeOfModification = Now.asDateTime()
    }

    fun asPersistentDto(id: Long?): PersistentDto {
        return PersistentDto(id, createdBy, timeOfCreation, modifiedBy, timeOfModification)
    }
}
