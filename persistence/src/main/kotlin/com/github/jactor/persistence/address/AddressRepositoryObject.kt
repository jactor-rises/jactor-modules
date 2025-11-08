package com.github.jactor.persistence.address

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

    override fun findByZipCode(zipCode: String): List<AddressDao> = Addresses
        .selectAll()
        .andWhere { Addresses.zipCode eq zipCode }
        .map { it.toAddressDao() }

    private fun ResultRow.toAddressDao(): AddressDao = AddressDao(
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

    override fun save(addressDao: AddressDao): AddressDao = when (addressDao.isNotPersisted) {
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

    private fun update(addressDao: AddressDao): AddressDao = Addresses.update(
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
