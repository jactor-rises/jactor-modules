package com.github.jactor.rises.persistence.person

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.shared.api.PersonDto
import java.util.UUID

@JvmRecord
data class Person(
    val persistent: Persistent,
    val addressId: UUID?,
    val locale: String?,
    val firstName: String?,
    val surname: String,
    val description: String?,
) {
    val id: UUID? @JsonIgnore get() = persistent.id

    fun toPersonDao() = PersonDao(
        id = persistent.id,
        createdBy = persistent.createdBy,
        timeOfCreation = persistent.timeOfCreation,
        modifiedBy = persistent.modifiedBy,
        timeOfModification = persistent.timeOfModification,
        description = description,
        firstName = firstName,
        surname = surname,
        locale = locale,
        addressId = addressId,
    )

    fun toPersonDto() = PersonDto(
        persistentDto = persistent.toPersistentDto(),
        addressId = addressId,
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
}
