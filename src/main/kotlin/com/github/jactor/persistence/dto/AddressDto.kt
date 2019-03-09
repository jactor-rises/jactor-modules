package com.github.jactor.persistence.dto

data class AddressDto(
        var persistentDto: PersistentDto? = null,
        var zipCode: Int? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, address: AddressDto
    ) : this(
            persistent,
            address.zipCode, address.addressLine1, address.addressLine2, address.addressLine3, address.city, address.country
    )

    override fun fetchPersistentDto(): PersistentDto {
        return if (persistentDto != null) persistentDto as PersistentDto else PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
