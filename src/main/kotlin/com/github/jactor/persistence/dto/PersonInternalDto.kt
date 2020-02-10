package com.github.jactor.persistence.dto

data class PersonInternalDto(
        var persistentDto: PersistentDto? = null,
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, personInternal: PersonInternalDto
    ) : this(
            persistent, personInternal.address, personInternal.locale, personInternal.firstName, personInternal.surname, personInternal.description
    )
}
