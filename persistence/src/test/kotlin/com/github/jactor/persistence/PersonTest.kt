package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.withId
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class PersonTest {

    @Test
    fun `should have a copy constructor`() {
        val person = initPerson(
            address = initAddress().withId(),
            firstName = "first name",
            description = "description",
            locale = "no",
            surname = "surname",
        )

        val (_, addressId, locale, firstName, surname, description) = person.copy(
            persistent = person.persistent,
        )

        assertAll {
            assertThat(addressId).isEqualTo(person.addressId)
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

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = initPerson(
            persistent = persistent,
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
