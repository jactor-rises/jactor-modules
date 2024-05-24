package com.github.jactor.persistence.dto

import com.github.jactor.shared.api.PersonDto

data class PersonInternalDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var address: AddressInternalDto? = null,
    var locale: String? = null,
    var firstName: String? = null,
    var surname: String = "",
    var description: String? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, personInternal: PersonInternalDto
    ) : this(
        persistentDto = persistentDto,
        address = personInternal.address,
        description = personInternal.description,
        firstName = personInternal.firstName,
        locale = personInternal.locale,
        surname = personInternal.surname
    )

    constructor(personDto: PersonDto) : this(
        persistentDto = PersistentDto(id = personDto.id),
        address = if (personDto.address != null) AddressInternalDto(personDto.address!!) else null,
        description = personDto.description,
        firstName = personDto.firstName,
        locale = personDto.locale,
        surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
        id = persistentDto.id,
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
}
