package com.github.jactor.persistence.dto

import com.github.jactor.shared.dto.AddressDto

data class AddressInternalDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var zipCode: String? = null,
    var addressLine1: String? = null,
    var addressLine2: String? = null,
    var addressLine3: String? = null,
    var city: String? = null,
    var country: String? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, addressInternal: AddressInternalDto
    ) : this(
        persistentDto = persistentDto,
        addressLine1 = addressInternal.addressLine1,
        addressLine2 = addressInternal.addressLine2,
        addressLine3 = addressInternal.addressLine3,
        city = addressInternal.city,
        country = addressInternal.country,
        zipCode = addressInternal.zipCode
    )

    constructor(addressDto: AddressDto) : this(
        persistentDto = PersistentDto(id = addressDto.id),
        addressLine1 = addressDto.addressLine1,
        addressLine2 = addressDto.addressLine2,
        addressLine3 = addressDto.addressLine3,
        city = addressDto.city,
        country = addressDto.country,
        zipCode = addressDto.zipCode
    )

    fun toAddressDto() = AddressDto(
        id = persistentDto.id,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )
}
