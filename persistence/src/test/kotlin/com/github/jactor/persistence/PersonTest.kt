package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class PersonTest {

    @Test
    fun `should have a copy constructor`() {
        val person = Person(
            address = Address(),
            description = "description",
            firstName = "first name",
            locale = "no",
            surname = "surname"
        )

        val (_, address, locale, firstName, surname, description) = Person(
            person.persistent,
            person
        )

        assertAll {
            assertThat(address).isEqualTo(person.address)
            assertThat(description).isEqualTo(person.description)
            assertThat(firstName).isEqualTo(person.firstName)
            assertThat(locale).isEqualTo(person.locale)
            assertThat(surname).isEqualTo(person.surname)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistent = Persistent(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfModification = LocalDateTime.now(),
            timeOfCreation = LocalDateTime.now()
        )

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = Person(
            persistent = persistent,
            person = Person()
        ).persistent

        assertAll {
            assertThat(createdBy).isEqualTo(persistent.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistent.timeOfCreation)
            assertThat(id).isEqualTo(persistent.id)
            assertThat(modifiedBy).isEqualTo(persistent.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistent.timeOfModification)
        }
    }

    @Test
    fun `should get address for person`() {
        val person = Person(
            address = Address(
                addressLine1 = "somewhere",
                addressLine2 = "in",
                addressLine3 = "time",
                city = "out there",
                zipCode = "1234"
            )
        )

        val address = Person(person.persistent, person).toPersonDto().address

        assertAll {
            assertThat(address?.addressLine1).isEqualTo("somewhere")
            assertThat(address?.addressLine2).isEqualTo("in")
            assertThat(address?.addressLine3).isEqualTo("time")
            assertThat(address?.city).isEqualTo("out there")
            assertThat(address?.zipCode).isEqualTo("1234")
        }
    }
}
