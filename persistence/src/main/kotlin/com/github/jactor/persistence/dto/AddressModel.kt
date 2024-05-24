package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.shared.api.AddressDto

data class AddressModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var zipCode: String? = null,
    var addressLine1: String? = null,
    var addressLine2: String? = null,
    var addressLine3: String? = null,
    var city: String? = null,
    var country: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, addressInternal: AddressModel
    ) : this(
        persistentModel = persistentModel,
        addressLine1 = addressInternal.addressLine1,
        addressLine2 = addressInternal.addressLine2,
        addressLine3 = addressInternal.addressLine3,
        city = addressInternal.city,
        country = addressInternal.country,
        zipCode = addressInternal.zipCode
    )

    constructor(addressDto: AddressDto) : this(
        persistentModel = PersistentModel(id = addressDto.id),
        addressLine1 = addressDto.addressLine1,
        addressLine2 = addressDto.addressLine2,
        addressLine3 = addressDto.addressLine3,
        city = addressDto.city,
        country = addressDto.country,
        zipCode = addressDto.zipCode
    )

    fun toAddressDto() = AddressDto(
        id = persistentModel.id,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )
}
