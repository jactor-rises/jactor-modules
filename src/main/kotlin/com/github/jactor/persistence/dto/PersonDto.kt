package com.github.jactor.persistence.dto

data class PersonDto(
        var persistentDto: PersistentDto? = null,
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, person: PersonDto
    ) : this(
            persistent, person.address, person.locale, person.firstName, person.surname, person.description
    )
}
