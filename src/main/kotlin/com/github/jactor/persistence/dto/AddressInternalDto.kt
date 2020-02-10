package com.github.jactor.persistence.dto

data class AddressInternalDto(
        var persistentDto: PersistentDto? = null,
        var zipCode: String? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, addressInternal: AddressInternalDto
    ) : this(
            persistent, addressInternal.zipCode, addressInternal.addressLine1, addressInternal.addressLine2, addressInternal.addressLine3, addressInternal.city, addressInternal.country
    )
}
