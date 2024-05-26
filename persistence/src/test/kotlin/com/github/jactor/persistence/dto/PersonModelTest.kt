package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.address.AddressModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class PersonModelTest {

    @Test
    fun `should have a copy constructor`() {
        val personModel = PersonModel(
            address = AddressModel(),
            description = "description",
            firstName = "first name",
            locale = "no",
            surname = "surname"
        )

        val (_, address, locale, firstName, surname, description) = PersonModel(
            personModel.persistentModel,
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
        val persistentModel = PersistentModel(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfModification = LocalDateTime.now(),
            timeOfCreation = LocalDateTime.now()
        )

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = PersonModel(
            persistentModel = persistentModel,
            person = PersonModel()
        ).persistentModel

        assertAll {
            assertThat(createdBy).isEqualTo(persistentModel.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentModel.timeOfCreation)
            assertThat(id).isEqualTo(persistentModel.id)
            assertThat(modifiedBy).isEqualTo(persistentModel.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentModel.timeOfModification)
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

        val address = PersonModel(personModel.persistentModel, personModel).toPersonDto().address

        assertAll {
            assertThat(address?.addressLine1).isEqualTo("somewhere")
            assertThat(address?.addressLine2).isEqualTo("in")
            assertThat(address?.addressLine3).isEqualTo("time")
            assertThat(address?.city).isEqualTo("out there")
            assertThat(address?.zipCode).isEqualTo("1234")
        }
    }
}
