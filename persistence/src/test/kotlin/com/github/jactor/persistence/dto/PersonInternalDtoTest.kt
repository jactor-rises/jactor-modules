package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class PersonInternalDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val personInternalDto = PersonInternalDto()
        personInternalDto.address = AddressInternalDto()
        personInternalDto.description = "description"
        personInternalDto.firstName = "first name"
        personInternalDto.locale = "no"
        personInternalDto.surname = "surname"

        val (_, address, locale, firstName, surname, description) = PersonInternalDto(
            personInternalDto.persistentDto,
            personInternalDto
        )

        assertAll {
            assertThat(address).isEqualTo(personInternalDto.address)
            assertThat(description).isEqualTo(personInternalDto.description)
            assertThat(firstName).isEqualTo(personInternalDto.firstName)
            assertThat(locale).isEqualTo(personInternalDto.locale)
            assertThat(surname).isEqualTo(personInternalDto.surname)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = UUID.randomUUID()
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = PersonInternalDto(
            persistentDto,
            PersonInternalDto()
        ).persistentDto

        assertAll {
            assertThat(createdBy).isEqualTo(persistentDto.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentDto.timeOfCreation)
            assertThat(id).isEqualTo(persistentDto.id)
            assertThat(modifiedBy).isEqualTo(persistentDto.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentDto.timeOfModification)
        }
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

        assertAll {
            assertThat(address?.addressLine1).isEqualTo("somewhere")
            assertThat(address?.addressLine2).isEqualTo("in")
            assertThat(address?.addressLine3).isEqualTo("time")
            assertThat(address?.city).isEqualTo("out there")
            assertThat(address?.zipCode).isEqualTo("1234")
        }
    }
}
