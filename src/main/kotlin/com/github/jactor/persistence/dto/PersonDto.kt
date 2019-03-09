package com.github.jactor.persistence.dto

data class PersonDto(
        var persistentDto: PersistentDto? = null,
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, person: PersonDto
    ) : this(
            persistent, person.address, person.locale, person.firstName, person.surname, person.description
    )

    override fun fetchPersistentDto(): PersistentDto {
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        return persistentDto as PersistentDto
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

     override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
