package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class PersonInternalDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val personInternalDto = PersonInternalDto()
        personInternalDto.address = AddressInternalDto()
        personInternalDto.description = "description"
        personInternalDto.firstName = "first name"
        personInternalDto.locale = "no"
        personInternalDto.surname = "surname"

        val (_, address, locale, firstName, surname, description) = PersonInternalDto(personInternalDto.persistentDto, personInternalDto)

        assertAll(
            { assertThat(address).`as`("address").isEqualTo(personInternalDto.address) },
            { assertThat(description).`as`("description").isEqualTo(personInternalDto.description) },
            { assertThat(firstName).`as`("first name").isEqualTo(personInternalDto.firstName) },
            { assertThat(locale).`as`("locale").isEqualTo(personInternalDto.locale) },
            { assertThat(surname).`as`("surname").isEqualTo(personInternalDto.surname) }
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = PersonInternalDto(persistentDto, PersonInternalDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }

    @Test
    fun `should get address for person`() {
        val personInternalDto = PersonInternalDto(
            address = AddressInternalDto(
                addressLine1 = "somewhere",
                addressLine2 = "in",
                addressLine3 = "time",
                city = "out there",
                zipCode = "1234"
            )
        )

        val address = PersonInternalDto(personInternalDto.persistentDto, personInternalDto).toPersonDto().address

        assertAll(
            { assertThat(address?.addressLine1).`as`("address.addressLine1").isEqualTo("somewhere") },
            { assertThat(address?.addressLine2).`as`("address.addressLine2").isEqualTo("in") },
            { assertThat(address?.addressLine3).`as`("address.addressLine3").isEqualTo("time") },
            { assertThat(address?.city).`as`("address.city").isEqualTo("out there") },
            { assertThat(address?.zipCode).`as`("address.zipCode").isEqualTo("1234") }
        )
    }
}
