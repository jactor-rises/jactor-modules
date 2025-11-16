package com.github.jactor.rises.persistence.address

import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

object AddressRepositoryObject : AddressRepository {
    override fun findById(id: UUID): AddressDao? = Addresses
        .selectAll()
        .andWhere { Addresses.id eq id }
        .singleOrNull()
        ?.toAddressDao()
}

internal fun ResultRow.toAddressDao(): AddressDao = AddressDao(
    id = this[Addresses.id].value,
    createdBy = this[Addresses.createdBy],
    timeOfCreation = this[Addresses.timeOfCreation],
    modifiedBy = this[Addresses.modifiedBy],
    timeOfModification = this[Addresses.timeOfModification],

    addressLine1 = this[Addresses.addressLine1],
    addressLine2 = this[Addresses.addressLine2],
    addressLine3 = this[Addresses.addressLine3],
    city = this[Addresses.city],
    country = this[Addresses.country],
    zipCode = this[Addresses.zipCode],
)
