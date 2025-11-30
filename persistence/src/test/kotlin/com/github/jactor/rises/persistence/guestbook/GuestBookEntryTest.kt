package com.github.jactor.rises.persistence.guestbook

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.test.initGuestBook
import com.github.jactor.rises.persistence.test.initGuestBookEntry
import com.github.jactor.rises.persistence.test.withId
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class GuestBookEntryTest {
    @Test
    fun `should have a copy constructor`() {
        val guestBookEntry = initGuestBookEntry(
            creatorName = "me",
            entry = "entry",
            guestBook = initGuestBook().withId(),
        )

        val (entry, creatorName, guestBookId) = GuestBookEntry(
            guestName = guestBookEntry.guestName,
            entry = guestBookEntry.entry,
            persistent = guestBookEntry.persistent,
            guestBookId = guestBookEntry.guestBookId,
        )

        assertAll {
            assertThat(creatorName).isEqualTo(guestBookEntry.guestName)
            assertThat(entry).isEqualTo(guestBookEntry.entry)
            assertThat(guestBookId).isEqualTo(guestBookEntry.guestBookId)
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

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = GuestBookEntry(
            guestName = persistent.createdBy,
            entry = "entry",
            persistent = persistent,
            guestBookId = null,
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
