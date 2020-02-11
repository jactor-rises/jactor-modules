package com.github.jactor.persistence.dto

import com.github.jactor.shared.dto.PersonDto

data class PersonInternalDto(
        var persistentDto: PersistentDto? = null,
        var address: AddressInternalDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, personInternal: PersonInternalDto
    ) : this(
            persistentDto = persistent,

            address = personInternal.address,
            description = personInternal.description,
            firstName = personInternal.firstName,
            locale = personInternal.locale,
            surname = personInternal.surname
    )

    constructor(personDto: PersonDto) : this(
            persistentDto = if (personDto.id != null) PersistentDto(id = personDto.id) else null,

            address = if (personDto.address != null) AddressInternalDto(personDto.address!!) else null,
            description = personDto.description,
            firstName = personDto.firstName,
            locale = personDto.locale,
            surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
            id = persistentDto?.id,
            address = address?.toAddressDto(),
            locale = locale,
            firstName = firstName,
            surname = surname,
            description = description
    )
}
