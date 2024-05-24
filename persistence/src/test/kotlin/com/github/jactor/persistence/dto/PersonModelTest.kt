package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class PersonModelTest {

    @Test
    fun `should have a copy constructor`() {
        val personModel = PersonModel()
        personModel.address = AddressModel()
        personModel.description = "description"
        personModel.firstName = "first name"
        personModel.locale = "no"
        personModel.surname = "surname"

        val (_, address, locale, firstName, surname, description) = PersonModel(
            personModel.persistentDto,
            personModel
        )

        assertAll {
            assertThat(address).isEqualTo(personModel.address)
            assertThat(description).isEqualTo(personModel.description)
            assertThat(firstName).isEqualTo(personModel.firstName)
            assertThat(locale).isEqualTo(personModel.locale)
            assertThat(surname).isEqualTo(personModel.surname)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = PersonModel(
            persistentDto,
            PersonModel()
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
        val personModel = PersonModel(
            address = AddressModel(
                addressLine1 = "somewhere",
                addressLine2 = "in",
                addressLine3 = "time",
                city = "out there",
                zipCode = "1234"
            )
        )

        val address = PersonModel(personModel.persistentDto, personModel).toPersonDto().address

        assertAll {
            assertThat(address?.addressLine1).isEqualTo("somewhere")
            assertThat(address?.addressLine2).isEqualTo("in")
            assertThat(address?.addressLine3).isEqualTo("time")
            assertThat(address?.city).isEqualTo("out there")
            assertThat(address?.zipCode).isEqualTo("1234")
        }
    }
}
