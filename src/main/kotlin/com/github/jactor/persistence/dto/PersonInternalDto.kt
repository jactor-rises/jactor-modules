package com.github.jactor.persistence.dto

data class PersonInternalDto(
        var persistentDto: PersistentDto? = null,
        var addressInternal: AddressInternalDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, personInternal: PersonInternalDto
    ) : this(
            persistent, personInternal.addressInternal, personInternal.locale, personInternal.firstName, personInternal.surname, personInternal.description
    )

    constructor(persistentDto: PersonDto) : this(

    )

    fun toUserDto() = com.github.jactor.shared.dto.PersonDto(

    )
}
