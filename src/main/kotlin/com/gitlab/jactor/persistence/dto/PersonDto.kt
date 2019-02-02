package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class PersonDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : AsPersistentDto {
    constructor(
            person: PersonDto
    ) : this(
            person.id, person.createdBy, person.creationTime, person.updatedBy, person.updatedTime,
            person.address, person.locale, person.firstName, person.surname, person.description
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
