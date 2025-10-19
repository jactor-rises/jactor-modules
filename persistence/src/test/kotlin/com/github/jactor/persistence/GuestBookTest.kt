package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class GuestBookTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBook = initGuestBook(
            title = "title",
            user = initUser().withId()
        )

        val (_, title, userId) = GuestBook(guestBook.persistent, guestBook)

        assertAll {
            assertThat(title).isEqualTo(guestBook.title)
            assertThat(userId).isEqualTo(guestBook.userId)
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

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = GuestBook(
            persistent, guestBook = initGuestBook()
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
