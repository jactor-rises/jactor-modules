package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest

internal class GuestBookServiceTest {

    private val guestBookRepositoryMockk: GuestBookRepository = mockk {}

    private val guestBookServiceToTest: GuestBookService = GuestBookServiceBean(
        guestBookRepository = guestBookRepositoryMockk
    )

    private val uuid = UUID.randomUUID()

    @Test
    fun `should map guest book to a dto`() = runTest {
        val guestBookDao = initGuestBook(title = "@home").toGuestBookDao()

        every { guestBookRepositoryMockk.findGuestBookById(id = uuid) } returns guestBookDao

        val (_, _, title) = guestBookServiceToTest.findGuestBook(id = uuid) ?: error("missed mocking?")

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() = runTest {
        val anEntry = initGuestBookEntry(
            guestBook = initGuestBook(),
            creatorName = "me",
            entry = "too",
            persistent = Persistent(),
        ).toGuestBookEntryDao()

        every { guestBookRepositoryMockk.findGuestBookEntryById(id = uuid) } returns anEntry

        val guestBookEntryModel = guestBookServiceToTest.findEntry(id = uuid) ?: error("missed mocking?")

        assertAll {
            assertThat(guestBookEntryModel.creatorName).isEqualTo("me")
            assertThat(guestBookEntryModel.entry).isEqualTo("too")
        }
    }

    @Test
    fun `should save GuestBookDto as GuestBookEntity`() = runTest {
        val guestBookEntry = initGuestBookEntry(
            creatorName = "me",
            entry = "all about this",
            guestBook = initGuestBook()
        )

        val guestBookEntitySlot = slot<GuestBookDao>()
        val guestBook = initGuestBook(
            entries = setOf(guestBookEntry),
            title = "home sweet home",
            user = initUser()
        )

        every {
            guestBookRepositoryMockk.save(guestBookDao = capture(guestBookEntitySlot))
        } returns guestBook.toGuestBookDao()

        guestBookServiceToTest.saveOrUpdate(guestBook)
        val guestBookEntity = guestBookEntitySlot.captured

        assertAll {
            assertThat(guestBookEntity.entries).hasSize(1)
            assertThat(guestBookEntity.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity.user).isNotNull()
        }
    }

    @Test
    fun `should save GuestBookEntryDto as GuestBookEntryEntity`() = runTest {
        val guestBookEntryEntitySlot = slot<GuestBookEntryDao>()
        val guestBookEntry = initGuestBookEntry(
            guestBook = initGuestBook(),
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every {
            guestBookRepositoryMockk.save(guestBookEntryDao = capture(guestBookEntryEntitySlot))
        } returns guestBookEntry.toGuestBookEntryDao()

        guestBookServiceToTest.saveOrUpdate(guestBookEntry)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBookDao).isNotNull()
            assertThat(guestBookEntryEntity.creatorName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
