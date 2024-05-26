package com.github.jactor.persistence.person

import java.util.UUID

internal object PersonBuilder {
    fun new(personModel: PersonModel = PersonModel()): PersonData = PersonData(
        personModel = personModel.copy(
            persistentModel = personModel.persistentModel.copy(id = UUID.randomUUID())
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
