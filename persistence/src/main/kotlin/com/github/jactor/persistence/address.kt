package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Repository
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
    constructor(addressDto: AddressDto) : this(
        persistent = Persistent(persistentDto = addressDto.persistentDto),

        addressLine1 = addressDto.addressLine1 ?: "",
        addressLine2 = addressDto.addressLine2,
        addressLine3 = addressDto.addressLine3,
        city = addressDto.city ?: "",
        country = addressDto.country,
        zipCode = addressDto.zipCode ?: ""
    )

    constructor(dao: AddressDao) : this(
        persistent = dao.toPersistent(),

        addressLine1 = dao.addressLine1,
        addressLine2 = dao.addressLine2,
        addressLine3 = dao.addressLine3,
        city = dao.city,
        country = dao.country,
        zipCode = dao.zipCode
    )

    fun toDto(): AddressDto = AddressDto(
        persistentDto = persistent.toDto(),

        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )

    fun toDao(): AddressDao = AddressDao(this)
}

object Addresses : UUIDTable("T_ADDRESS", "ID") {
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

@Repository
class AddressRepository {
    fun findByZipCode(zip: String): List<AddressDao> = transaction {
        Addresses
            .selectAll()
            .andWhere { Addresses.zipCode eq zip }
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
    constructor(address: Address) : this(
        id = address.persistent.id,
        createdBy = address.persistent.createdBy,
        timeOfCreation = address.persistent.timeOfCreation,
        modifiedBy = address.persistent.modifiedBy,
        timeOfModification = address.persistent.timeOfModification,
        addressLine1 = address.addressLine1,
        addressLine2 = address.addressLine2,
        addressLine3 = address.addressLine3,
        city = address.city,
        country = address.country,
        zipCode = address.zipCode,
    )

    override fun copyWithoutId(): AddressDao = copy(id = null)
    override fun modifiedBy(modifier: String): AddressDao = copy(
        modifiedBy = modifier,
        timeOfModification = LocalDateTime.now()
    )

    fun toAddress(): Address = Address(this)
}
