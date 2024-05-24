package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.shared.api.PersonDto

data class PersonModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var address: AddressModel? = null,
    var locale: String? = null,
    var firstName: String? = null,
    var surname: String = "",
    var description: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, personInternal: PersonModel
    ) : this(
        persistentModel = persistentModel,
        address = personInternal.address,
        description = personInternal.description,
        firstName = personInternal.firstName,
        locale = personInternal.locale,
        surname = personInternal.surname
    )

    constructor(personDto: PersonDto) : this(
        persistentModel = PersistentModel(id = personDto.id),
        address = if (personDto.address != null) AddressModel(personDto.address!!) else null,
        description = personDto.description,
        firstName = personDto.firstName,
        locale = personDto.locale,
        surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
        id = persistentModel.id,
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
}
