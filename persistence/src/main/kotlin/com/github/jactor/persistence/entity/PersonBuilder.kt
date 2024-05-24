package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.PersonModel

internal object PersonBuilder {
    fun new(personModel: PersonModel = PersonModel()): PersonData = PersonData(
        personModel = personModel.copy(
            persistentDto = personModel.persistentDto.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(personModel: PersonModel): PersonData = PersonData(
        personModel = personModel
    )

    @JvmRecord
    data class PersonData(val personModel: PersonModel) {
        fun build(): PersonEntity = PersonEntity(person = personModel)
    }
}
