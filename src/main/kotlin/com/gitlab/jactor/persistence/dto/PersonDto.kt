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
            persistent: PersistentDto,
            address: AddressDto?, locale: String?, firstName: String?, surname: String?, description: String?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            address, locale, firstName, surname, description
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
