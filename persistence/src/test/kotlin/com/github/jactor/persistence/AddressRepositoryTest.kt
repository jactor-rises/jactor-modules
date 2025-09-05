package com.github.jactor.persistence

import kotlin.jvm.optionals.getOrNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isIn
import assertk.assertions.isPresent

internal class AddressRepositoryTest @Autowired constructor(
    private val addressRepository: AddressRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should fetch address entities`() {
        flush {
            addressRepository.save(
                initAddress(
                    addressLine1 = "somewhere out there",
                    city = "Rud",
                    zipCode = "1234",
                ).toEntityWithId()
            )

            addressRepository.save(
                initAddress(
                    addressLine1 = "somewhere in there",
                    city = "Rud",
                    zipCode = "1234",
                ).toEntityWithId()
            )
        }

        val addressEntities = addressRepository.findByZipCode(zipCode = "1234")

        assertAll {
            assertThat(addressEntities).hasSize(2)
            addressEntities.forEach {
                assertThat(it.addressLine1).isIn("somewhere out there", "somewhere in there")
            }
        }
    }

    @Test
    fun `should write then read an address entity`() {
        val addressEntityToPersist = initAddress(
            addressLine1 = "somewhere out there",
            addressLine2 = "where the streets have no name",
            addressLine3 = "in the middle of it",
            city = "Rud",
            country = "NO",
            zipCode = "1234",
        ).toEntityWithId()

        flush { addressRepository.save(addressEntityToPersist) }

        val possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.id!!)

        assertThat(possibleAddressEntityById).isPresent().given { addressEntity: AddressEntity ->
            assertAll {
                assertThat(addressEntity.addressLine1).isEqualTo("somewhere out there")
                assertThat(addressEntity.addressLine2).isEqualTo("where the streets have no name")
                assertThat(addressEntity.addressLine3).isEqualTo("in the middle of it")
                assertThat(addressEntity.zipCode).isEqualTo("1234")
                assertThat(addressEntity.country).isEqualTo("NO")
                assertThat(addressEntity.city).isEqualTo("Rud")
            }
        }
    }

    @Test
    fun `should write then update and read an address entity`() {
        val addressEntityToPersist = initAddress(
            addressLine1 = "somewhere out there",
            addressLine2 = "where the streets have no name",
            addressLine3 = "in the middle of it",
            city = "Rud",
            country = "NO",
            zipCode = "1234",
        ).toEntityWithId()

        flush { addressRepository.save(addressEntityToPersist) }

        val addressEntitySaved = addressRepository.findById(addressEntityToPersist.id!!)
            .getOrNull() ?: throwAddressNotFound()

        addressEntitySaved.addressLine1 = "the truth is out there"
        addressEntitySaved.addressLine2 = "among the stars"
        addressEntitySaved.addressLine3 = "there will be life"
        addressEntitySaved.zipCode = "666"
        addressEntitySaved.city = "Cloud city"
        addressEntitySaved.country = "XX"

        flush { addressRepository.save(addressEntitySaved) }

        val possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.id!!)
            .getOrNull() ?: throwAddressNotFound()

        assertThat(possibleAddressEntityById).given { addressEntity: AddressEntity ->
            assertAll {
                assertThat(addressEntity.addressLine1).isEqualTo("the truth is out there")
                assertThat(addressEntity.addressLine2).isEqualTo("among the stars")
                assertThat(addressEntity.addressLine3).isEqualTo("there will be life")
                assertThat(addressEntity.zipCode).isEqualTo("666")
                assertThat(addressEntity.country).isEqualTo("XX")
                assertThat(addressEntity.city).isEqualTo("Cloud city")
            }
        }
    }

    private fun throwAddressNotFound(): Nothing {
        throw AssertionError("address not found")
    }
}
