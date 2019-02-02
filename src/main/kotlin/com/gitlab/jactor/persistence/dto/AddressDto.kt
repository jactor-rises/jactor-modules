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
            address: AddressDto
    ) : this(
            address.id, address.createdBy, address.creationTime, address.updatedBy, address.updatedTime,
            address.zipCode, address.addressLine1, address.addressLine2, address.addressLine3, address.city, address.country
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
