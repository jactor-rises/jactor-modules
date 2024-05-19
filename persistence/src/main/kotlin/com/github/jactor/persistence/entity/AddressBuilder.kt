package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.AddressInternalDto

internal object AddressBuilder {
    fun new(addressInternalDto: AddressInternalDto) = AddressData(
        addressInternalDto = addressInternalDto.copy(
            persistentDto = addressInternalDto.persistentDto.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(addressInternalDto: AddressInternalDto): AddressData = AddressData(
        addressInternalDto = addressInternalDto
    )

    @JvmRecord
    data class AddressData(val addressInternalDto: AddressInternalDto) {
        fun build(): AddressEntity = AddressEntity(addressInternalDto = addressInternalDto)
    }
}
