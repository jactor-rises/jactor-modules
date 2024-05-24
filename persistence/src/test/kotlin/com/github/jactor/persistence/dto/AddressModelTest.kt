package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class AddressModelTest {

    @Test
    fun `hould have a copy constructor`() {
        val addressModel = AddressModel()
        addressModel.addressLine1 = "address line one"
        addressModel.addressLine2 = "address line two"
        addressModel.addressLine3 = "address line three"
        addressModel.city = "oslo"
        addressModel.country = "NO"
        addressModel.zipCode = "1234"

        val (_, zipCode, addressLine1, addressLine2, addressLine3, city, country) = AddressModel(
            addressModel.persistentModel,
            addressModel
        )

        assertAll {
            assertThat(addressLine1).isEqualTo(addressModel.addressLine1)
            assertThat(addressLine2).isEqualTo(addressModel.addressLine2)
            assertThat(addressLine3).isEqualTo(addressModel.addressLine3)
            assertThat(city).isEqualTo(addressModel.city)
            assertThat(country).isEqualTo(addressModel.country)
            assertThat(zipCode).isEqualTo(addressModel.zipCode)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentModel = PersistentModel()
        persistentModel.createdBy = "jactor"
        persistentModel.timeOfCreation = LocalDateTime.now()
        persistentModel.id = UUID.randomUUID()
        persistentModel.modifiedBy = "tip"
        persistentModel.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = AddressModel(
            persistentModel, AddressModel()
        ).persistentModel

        assertAll {
            assertThat(createdBy).isEqualTo(persistentModel.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentModel.timeOfCreation)
            assertThat(id).isEqualTo(persistentModel.id)
            assertThat(modifiedBy).isEqualTo(persistentModel.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentModel.timeOfModification)
        }
    }
}
