package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.AddressModel

internal object AddressBuilder {
    fun new(addressModel: AddressModel) = AddressData(
        addressModel = addressModel.copy(
            persistentModel = addressModel.persistentModel.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(addressModel: AddressModel): AddressData = AddressData(
        addressModel = addressModel
    )

    @JvmRecord
    data class AddressData(val addressModel: AddressModel) {
        fun build(): AddressEntity = AddressEntity(addressModel = addressModel)
    }
}
