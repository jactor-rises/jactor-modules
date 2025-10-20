package com.github.jactor.persistence.address

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.Persistent
import com.github.jactor.persistence.test.initAddress
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test

internal class AddressTest {

    @Test
    fun `should have a copy constructor`() {
        val address = initAddress(
            addressLine1 = "address line one",
            addressLine2 = "address line two",
            addressLine3 = "address line three",
            city = "oslo",
            country = "NO",
            zipCode = "1234"
        ).toAddressDao()

        val (_, addressLine1, addressLine2, addressLine3, city, country, zipCode) = Address(dao = address)

        assertAll {
            assertThat(addressLine1).isEqualTo(address.addressLine1)
            assertThat(addressLine2).isEqualTo(address.addressLine2)
            assertThat(addressLine3).isEqualTo(address.addressLine3)
            assertThat(city).isEqualTo(address.city)
            assertThat(country).isEqualTo(address.country)
            assertThat(zipCode).isEqualTo(address.zipCode)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistent = Persistent(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now(),
        )

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = Address(
            persistent = persistent,
            addressLine1 = "address line one",
            addressLine2 = "address line two",
            addressLine3 = "address line three",
            city = "oslo",
            country = "NO",
            zipCode = "1234"
        ).persistent

        assertAll {
            assertThat(createdBy).isEqualTo(persistent.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistent.timeOfCreation)
            assertThat(id).isEqualTo(persistent.id)
            assertThat(modifiedBy).isEqualTo(persistent.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistent.timeOfModification)
        }
    }
}
