package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.AddressInternalDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
import java.util.Objects

@Entity
@Table(name = "T_ADDRESS")
class AddressEntity : PersistentEntity<AddressEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSeq")
    @SequenceGenerator(name = "addressSeq", sequenceName = "T_ADDRESS_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

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

    @Suppress("UNUSED") constructor() {
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

    internal constructor(addressInternalDto: AddressInternalDto) {
        persistentDataEmbeddable = PersistentDataEmbeddable(addressInternalDto.persistentDto)
        addressLine1 = addressInternalDto.addressLine1
        addressLine2 = addressInternalDto.addressLine2
        addressLine3 = addressInternalDto.addressLine3
        city = addressInternalDto.city
        country = addressInternalDto.country
        id = addressInternalDto.id
        zipCode = addressInternalDto.zipCode
    }

    fun asDto(): AddressInternalDto {
        return AddressInternalDto(persistentDataEmbeddable.asPersistentDto(id), zipCode, addressLine1, addressLine2, addressLine3, city, country)
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

    override val createdBy: String
        get() = persistentDataEmbeddable.createdBy
    override val timeOfCreation: LocalDateTime
        get() = persistentDataEmbeddable.timeOfCreation
    override val modifiedBy: String
        get() = persistentDataEmbeddable.modifiedBy
    override val timeOfModification: LocalDateTime
        get() = persistentDataEmbeddable.timeOfModification

    companion object {
        fun anAddress(addressInternalDto: AddressInternalDto): AddressEntity {
            return AddressEntity(addressInternalDto)
        }
    }
}
