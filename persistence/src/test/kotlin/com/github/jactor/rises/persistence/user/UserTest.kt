package com.github.jactor.rises.persistence.user

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.test.initPerson
import com.github.jactor.rises.persistence.test.initUser
import com.github.jactor.rises.persistence.test.withId
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class UserTest {
    @Test
    fun `should have a copy constructor`() {
        val user = initUser(
            emailAddress = "somewhere@time",
            person = initPerson().withId(),
            username = "me",
        )

        val (_, emailAddress, personId, username) = user.copy(persistent = user.persistent)

        assertAll {
            assertThat(emailAddress).isEqualTo(user.emailAddress)
            assertThat(personId).isEqualTo(user.personId)
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
            timeOfModification = LocalDateTime.now(),
        )

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = initUser(
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
