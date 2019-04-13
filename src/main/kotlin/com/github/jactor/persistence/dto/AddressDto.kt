package com.github.jactor.persistence.dto

data class AddressDto(
        var persistentDto: PersistentDto? = null,
        var zipCode: Int? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, address: AddressDto
    ) : this(
            persistent, address.zipCode, address.addressLine1, address.addressLine2, address.addressLine3, address.city, address.country
    )
}
