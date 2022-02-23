package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

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

        assertAll(
            { assertThat(addressLine1).`as`("address line one").isEqualTo(addressInternalDto.addressLine1) },
            { assertThat(addressLine2).`as`("address line two").isEqualTo(addressInternalDto.addressLine2) },
            { assertThat(addressLine3).`as`("address line three").isEqualTo(addressInternalDto.addressLine3) },
            { assertThat(city).`as`("city").isEqualTo(addressInternalDto.city) },
            { assertThat(country).`as`("country").isEqualTo(addressInternalDto.country) },
            { assertThat(zipCode).`as`("zip code").isEqualTo(addressInternalDto.zipCode) }
        )
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = 1L
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = AddressInternalDto(persistentDto, AddressInternalDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
