package com.github.jactor.persistence.address

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.AddressDto

@JvmRecord
data class AddressModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val zipCode: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val addressLine3: String? = null,
    val city: String? = null,
    val country: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, addressModel: AddressModel
    ) : this(
        persistentModel = persistentModel,
        addressLine1 = addressModel.addressLine1,
        addressLine2 = addressModel.addressLine2,
        addressLine3 = addressModel.addressLine3,
        city = addressModel.city,
        country = addressModel.country,
        zipCode = addressModel.zipCode
    )

    constructor(addressDto: AddressDto) : this(
        persistentModel = PersistentModel(persistentDto = addressDto.persistentDto),
        addressLine1 = addressDto.addressLine1,
        addressLine2 = addressDto.addressLine2,
        addressLine3 = addressDto.addressLine3,
        city = addressDto.city,
        country = addressDto.country,
        zipCode = addressDto.zipCode
    )

    fun toAddressDto() = AddressDto(
        persistentDto = persistentModel.toDto(),
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )
}
