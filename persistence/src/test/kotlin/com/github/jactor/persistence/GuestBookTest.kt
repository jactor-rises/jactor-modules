package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBook = GuestBook(
            entries = setOf(GuestBookEntry()),
            title = "title",
            user = User()
        )

        val (_, entries, title, userInternal) = GuestBook(guestBook.persistent, guestBook)

        assertAll {
            assertThat(entries).isEqualTo(guestBook.entries)
            assertThat(title).isEqualTo(guestBook.title)
            assertThat(userInternal).isEqualTo(guestBook.user)
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

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = GuestBook(
            persistent, GuestBook()
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
