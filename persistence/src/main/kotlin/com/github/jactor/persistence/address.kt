package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.repository.CrudRepository
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.AddressDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@JvmRecord
data class Address(
    val persistent: Persistent,
    val zipCode: String?,
    val addressLine1: String?,
    val addressLine2: String?,
    val addressLine3: String?,
    val city: String?,
    val country: String?,
) {
    val id: UUID? @JsonIgnore get() = persistent.id

    constructor(
        persistent: Persistent, address: Address
    ) : this(
        persistent = persistent,
        addressLine1 = address.addressLine1,
        addressLine2 = address.addressLine2,
        addressLine3 = address.addressLine3,
        city = address.city,
        country = address.country,
        zipCode = address.zipCode
    )

    constructor(addressDto: AddressDto) : this(
        persistent = Persistent(persistentDto = addressDto.persistentDto),
        addressLine1 = addressDto.addressLine1,
        addressLine2 = addressDto.addressLine2,
        addressLine3 = addressDto.addressLine3,
        city = addressDto.city,
        country = addressDto.country,
        zipCode = addressDto.zipCode
    )

    fun toAddressDto() = AddressDto(
        persistentDto = persistent.toDto(),
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        city = city,
        country = country,
        zipCode = zipCode
    )

    fun withId(): Address = this.copy(persistent = persistent.copy(id = id ?: UUID.randomUUID()))
    fun toEntityWithId() = AddressEntity(
        this.copy(
            persistent = persistent.copy(id = id ?: UUID.randomUUID())
        )
    )
}

interface AddressRepository : CrudRepository<AddressEntity, UUID> {
    fun findByZipCode(zipCode: String): List<AddressEntity>
}

@Entity
@Table(name = "T_ADDRESS")
class AddressEntity : PersistentEntity<AddressEntity?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

    @Column(name = "ADDRESS_LINE_1", nullable = false)
    var addressLine1: String? = null

    @Column(name = "ADDRESS_LINE_2")
    var addressLine2: String? = null

    @Column(name = "ADDRESS_LINE_3")
    var addressLine3: String? = null

    @Column(name = "CITY", nullable = false)
    var city: String? = null

    @Column(name = "COUNTRY")
    var country: String? = null

    @Column(name = "ZIP_CODE", nullable = false)
    var zipCode: String? = null

    @Suppress("UNUSED")
    constructor() {
        // used by entity manager
    }

    /**
     * @param address to copyWithoutId
     */
    private constructor(address: AddressEntity) {
        persistentDataEmbeddable = PersistentDataEmbeddable()
        addressLine1 = address.addressLine1
        addressLine2 = address.addressLine2
        addressLine3 = address.addressLine3
        city = address.city
        country = address.country
        id = address.id
        zipCode = address.zipCode
    }

    internal constructor(address: Address) {
        persistentDataEmbeddable = PersistentDataEmbeddable(address.persistent)
        addressLine1 = address.addressLine1
        addressLine2 = address.addressLine2
        addressLine3 = address.addressLine3
        city = address.city
        country = address.country
        id = address.id
        zipCode = address.zipCode
    }

    fun toModel(): Address {
        return Address(
            persistentDataEmbeddable.toModel(id),
            zipCode,
            addressLine1,
            addressLine2,
            addressLine3,
            city,
            country
        )
    }

    override fun copyWithoutId(): AddressEntity {
        val addressEntity = AddressEntity(this)
        addressEntity.id = null
        return addressEntity
    }

    override fun modifiedBy(modifier: String): AddressEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val addressEntity = other as AddressEntity

        return this === other || addressLine1 == addressEntity.addressLine1 &&
            addressLine2 == addressEntity.addressLine2 &&
            addressLine3 == addressEntity.addressLine3 &&
            city == addressEntity.city &&
            country == addressEntity.country &&
            zipCode == addressEntity.zipCode
    }

    override fun hashCode(): Int {
        return Objects.hash(addressLine1, addressLine2, addressLine3, city, country, zipCode)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(addressLine1)
            .append(addressLine2)
            .append(addressLine3)
            .append(zipCode)
            .append(city)
            .append(country)
            .toString()
    }

    override val createdBy: String get() = persistentDataEmbeddable.createdBy
    override val timeOfCreation: LocalDateTime get() = persistentDataEmbeddable.timeOfCreation
    override val modifiedBy: String get() = persistentDataEmbeddable.modifiedBy
    override val timeOfModification: LocalDateTime get() = persistentDataEmbeddable.timeOfModification
}
