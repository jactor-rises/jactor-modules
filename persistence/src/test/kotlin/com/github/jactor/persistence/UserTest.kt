package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class UserTest {

    @Test
    fun `should have a copy constructor`() {
        val user = User(
            emailAddress = "somewhere@time",
            person = initPerson(),
            username = "me"
        )

        val (_, person, emailAddress, username) = User(user.persistent, user)

        assertAll {
            assertThat(emailAddress).isEqualTo(user.emailAddress)
            assertThat(person).isEqualTo(user.person)
            assertThat(username).isEqualTo(user.username)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistent = Persistent(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now()
        )

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = User(
            persistent,
            User()
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