package com.github.jactor.rises.persistence.address

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isIn
import assertk.assertions.isNotNull
import com.github.jactor.rises.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.rises.persistence.test.initAddress
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AddressRepositoryTest @Autowired constructor(
    private val addressRepository: AddressRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should fetch address entities by zip code`() {
        addressRepository.save(
            initAddress(
                addressLine1 = "somewhere out there",
                city = "Rud",
                zipCode = "1234",
            ).toAddressDao()
        )

        addressRepository.save(
            initAddress(
                addressLine1 = "somewhere in there",
                city = "Rud",
                zipCode = "1234",
            ).toAddressDao()
        )

        addressRepository.save(
            initAddress(
                addressLine1 = "on the road",
                city = "Out There",
                zipCode = "1001",
            ).toAddressDao()
        )

        val addresses = addressRepository.findByZipCode(zipCode = "1234")

        assertAll {
            assertThat(addresses).hasSize(2)
            addresses.forEach {
                assertThat(it.addressLine1).isIn("somewhere out there", "somewhere in there")
                assertThat(it.city).isEqualTo("Rud")
            }
        }
    }

    @Test
    fun `should write then read an address dao by id`() {
        val addressToPersist = initAddress(
            addressLine1 = "somewhere out there",
            addressLine2 = "where the streets have no name",
            addressLine3 = "in the middle of it",
            city = "Rud",
            country = "NO",
            zipCode = "1234",
        ).toAddressDao()

        addressRepository.save(addressToPersist)

        val possibleAddressById = addressRepository.findById(addressToPersist.id!!)

        assertThat(possibleAddressById).isNotNull().given { addressDao: AddressDao ->
            assertAll {
                assertThat(addressDao.addressLine1).isEqualTo("somewhere out there")
                assertThat(addressDao.addressLine2).isEqualTo("where the streets have no name")
                assertThat(addressDao.addressLine3).isEqualTo("in the middle of it")
                assertThat(addressDao.zipCode).isEqualTo("1234")
                assertThat(addressDao.country).isEqualTo("NO")
                assertThat(addressDao.city).isEqualTo("Rud")
            }
        }
    }

    @Test
    fun `should write then update and read an address dao`() {
        val addressToPersist = initAddress(
            addressLine1 = "somewhere out there",
            addressLine2 = "where the streets have no name",
            addressLine3 = "in the middle of it",
            city = "Rud",
            country = "NO",
            zipCode = "1234",
        ).toAddressDao()

        addressRepository.save(addressToPersist)

        val addressSaved = addressRepository.findById(id = addressToPersist.id!!) ?: addressNotFound()

        addressSaved.addressLine1 = "the truth is out there"
        addressSaved.addressLine2 = "among the stars"
        addressSaved.addressLine3 = "there will be life"
        addressSaved.zipCode = "666"
        addressSaved.city = "Cloud city"
        addressSaved.country = "XX"

        addressRepository.save(addressSaved)

        val possibleAddressById = addressRepository.findById(addressToPersist.id!!) ?: addressNotFound()

        assertThat(possibleAddressById).given { addressDao: AddressDao ->
            assertAll {
                assertThat(addressDao.addressLine1).isEqualTo("the truth is out there")
                assertThat(addressDao.addressLine2).isEqualTo("among the stars")
                assertThat(addressDao.addressLine3).isEqualTo("there will be life")
                assertThat(addressDao.zipCode).isEqualTo("666")
                assertThat(addressDao.country).isEqualTo("XX")
                assertThat(addressDao.city).isEqualTo("Cloud city")
            }
        }
    }

    private fun addressNotFound(): Nothing {
        throw AssertionError("address not found")
    }
}
