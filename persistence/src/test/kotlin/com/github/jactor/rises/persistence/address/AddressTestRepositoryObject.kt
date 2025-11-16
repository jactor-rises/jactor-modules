package com.github.jactor.rises.persistence.address

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import com.github.jactor.rises.persistence.util.toAddressDao

object AddressTestRepositoryObject {
    fun findByZipCode(zipCode: String): List<AddressDao> = Addresses
        .selectAll()
        .andWhere { Addresses.zipCode eq zipCode }
        .map { it.toAddressDao() }

    fun save(addressDao: AddressDao): AddressDao = when (addressDao.isNotPersisted) {
        true -> insert(addressDao)
        false -> update(addressDao)
    }

    private fun insert(addressDao: AddressDao): AddressDao = Addresses.insertAndGetId { row ->
        row[createdBy] = addressDao.createdBy
        row[modifiedBy] = addressDao.modifiedBy
        row[timeOfCreation] = addressDao.timeOfCreation
        row[timeOfModification] = addressDao.timeOfModification

        row[addressLine1] = addressDao.addressLine1
        row[addressLine2] = addressDao.addressLine2
        row[addressLine3] = addressDao.addressLine3
        row[city] = addressDao.city
        row[country] = addressDao.country
        row[zipCode] = addressDao.zipCode
    }.let { newId -> addressDao.also { it.id = newId.value } }

    internal fun update(addressDao: AddressDao): AddressDao = Addresses.update(
        { Addresses.id eq addressDao.id }
    ) { row ->
        row[modifiedBy] = addressDao.modifiedBy
        row[timeOfModification] = addressDao.timeOfModification

        row[addressLine1] = addressDao.addressLine1
        row[addressLine2] = addressDao.addressLine2
        row[addressLine3] = addressDao.addressLine3
        row[city] = addressDao.city
        row[country] = addressDao.country
        row[zipCode] = addressDao.zipCode
    }.let { addressDao }
}
