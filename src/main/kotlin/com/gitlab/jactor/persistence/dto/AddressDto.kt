package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class AddressDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var zipCode: Int? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : AsPersistentDto {
    constructor(
            persistent: PersistentDto,
            zipCode: Int?, addressLine1: String?, addressLine2: String?, addressLine3: String?,
            city: String?, country: String?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            zipCode, addressLine1, addressLine2, addressLine3, city, country
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
