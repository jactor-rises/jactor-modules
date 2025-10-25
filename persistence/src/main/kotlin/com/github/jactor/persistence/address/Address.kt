package com.github.jactor.persistence.address

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.Persistent

@JvmRecord
data class Address(
    internal val persistent: Persistent,

    val addressLine1: String,
    val addressLine2: String?,
    val addressLine3: String?,
    val city: String,
    val country: String?,
    val zipCode: String,
) {
    val id: UUID? @JsonIgnore get() = persistent.id

    constructor(dao: AddressDao) : this(
        persistent = dao.toPersistent(),

        addressLine1 = dao.addressLine1,
        addressLine2 = dao.addressLine2,
        addressLine3 = dao.addressLine3,
        city = dao.city,
        country = dao.country,
        zipCode = dao.zipCode
    )

    fun toAddressDao() = AddressDao(
        id = null,

        createdBy = persistent.createdBy,
        timeOfCreation = persistent.timeOfCreation,
        modifiedBy = persistent.modifiedBy,
        timeOfModification = persistent.timeOfModification,

        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode,
    )
}
