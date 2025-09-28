package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.AddressDto

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
    constructor(dao: AddressDao) : this(
        persistent = dao.toPersistent(),

        addressLine1 = dao.addressLine1,
        addressLine2 = dao.addressLine2,
        addressLine3 = dao.addressLine3,
        city = dao.city,
        country = dao.country,
        zipCode = dao.zipCode
    )

    fun toAddressDto(): AddressDto = AddressDto(
        persistentDto = persistent.toPersistentDto(),

        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
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

object Addresses : UUIDTable(name = "T_ADDRESS", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val addressLine1 = text("ADDRESS_LINE_1")
    val addressLine2 = text("ADDRESS_LINE_2").nullable()
    val addressLine3 = text("ADDRESS_LINE_3").nullable()
    val city = text("CITY")
    val country = text("COUNTRY").nullable()
    val zipCode = text("ZIP_CODE")
}

interface AddressRepository {
    fun findById(addressId: UUID): AddressDao?
    fun findByZipCode(zipCode: String): List<AddressDao>
    fun save(addressDao: AddressDao): AddressDao
}

object AddressRepositoryObject : AddressRepository {
    override fun findById(addressId: UUID): AddressDao? = transaction {
        Addresses
            .selectAll()
            .andWhere { Addresses.id eq addressId }
            .singleOrNull()
            ?.toAddressDao()
    }

    override fun findByZipCode(zipCode: String): List<AddressDao> = transaction {
        Addresses
            .selectAll()
            .andWhere { Addresses.zipCode eq zipCode }
            .map { it.toAddressDao() }
    }

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

    override fun save(addressDao: AddressDao): AddressDao = transaction {
        when (addressDao.isNotPersisted) {
            true -> insert(addressDao)
            false -> update(addressDao)
        }
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

data class AddressDao(
    override var id: UUID?,
    override val createdBy: String,
    override val timeOfCreation: LocalDateTime,
    override var modifiedBy: String,
    override var timeOfModification: LocalDateTime,

    var addressLine1: String,
    var addressLine2: String?,
    var addressLine3: String?,
    var city: String,
    var country: String?,
    var zipCode: String,
) : PersistentDao<AddressDao> {
    val isNotPersisted: Boolean get() = id == null

    override fun copyWithoutId(): AddressDao = copy(id = null)
    override fun modifiedBy(modifier: String): AddressDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toAddress() = Address(
        persistent = toPersistent(),

        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )
}
