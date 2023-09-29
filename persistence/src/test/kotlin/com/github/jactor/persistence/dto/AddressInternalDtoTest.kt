package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class AddressInternalDtoTest {

    @Test
    fun `hould have a copy constructor`() {
        val addressInternalDto = AddressInternalDto()
        addressInternalDto.addressLine1 = "address line one"
        addressInternalDto.addressLine2 = "address line two"
        addressInternalDto.addressLine3 = "address line three"
        addressInternalDto.city = "oslo"
        addressInternalDto.country = "NO"
        addressInternalDto.zipCode = "1234"

        val (_, zipCode, addressLine1, addressLine2, addressLine3, city, country) = AddressInternalDto(
            addressInternalDto.persistentDto,
            addressInternalDto
        )

        assertAll {
            assertThat(addressLine1).isEqualTo(addressInternalDto.addressLine1)
            assertThat(addressLine2).isEqualTo(addressInternalDto.addressLine2)
            assertThat(addressLine3).isEqualTo(addressInternalDto.addressLine3)
            assertThat(city).isEqualTo(addressInternalDto.city)
            assertThat(country).isEqualTo(addressInternalDto.country)
            assertThat(zipCode).isEqualTo(addressInternalDto.zipCode)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = 1L
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = AddressInternalDto(
            persistentDto, AddressInternalDto()
        ).persistentDto

        assertAll {
            assertThat(createdBy).isEqualTo(persistentDto.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentDto.timeOfCreation)
            assertThat(id).isEqualTo(persistentDto.id)
            assertThat(modifiedBy).isEqualTo(persistentDto.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentDto.timeOfModification)
        }
    }
}
