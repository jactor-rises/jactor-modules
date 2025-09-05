package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookEntryTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookEntry = initGuestBookEntry(
            creatorName = "me",
            entry = "entry",
            guestBook = initGuestBook()
        )

        val (creatorName, entry, guestBook) = GuestBookEntry(
            guestBookEntry.persistent,
            guestBookEntry
        )

        assertAll {
            assertThat(creatorName).isEqualTo(guestBookEntry.creatorName)
            assertThat(guestBook).isEqualTo(guestBookEntry.guestBook)
            assertThat(entry).isEqualTo(guestBookEntry.entry)
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

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = GuestBookEntry(
            persistent,
            initGuestBookEntry()
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
