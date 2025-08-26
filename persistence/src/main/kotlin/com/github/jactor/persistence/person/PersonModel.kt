package com.github.jactor.persistence.person

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.AddressModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.PersonDto

@JvmRecord
data class PersonModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val address: AddressModel? = null,
    val locale: String? = null,
    val firstName: String? = null,
    val surname: String = "",
    val description: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, person: PersonModel
    ) : this(
        persistentModel = persistentModel,
        address = person.address,
        description = person.description,
        firstName = person.firstName,
        locale = person.locale,
        surname = person.surname
    )

    constructor(personDto: PersonDto) : this(
        persistentModel = PersistentModel(persistentDto = personDto.persistentDto),
        address = if (personDto.address != null) AddressModel(personDto.address!!) else null,
        description = personDto.description,
        firstName = personDto.firstName,
        locale = personDto.locale,
        surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
        persistentDto = persistentModel.toDto(),
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
}
