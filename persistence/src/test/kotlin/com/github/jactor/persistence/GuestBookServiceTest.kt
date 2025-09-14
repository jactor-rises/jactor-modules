package com.github.jactor.persistence

import java.util.Optional
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
    private val guestBookEntryRepositoryMockk: GuestBookEntryRepository = mockk {}

    private val guestBookServiceToTest: GuestBookService = DefaultGuestBookService(
        guestBookRepository = guestBookRepositoryMockk,
        guestBookEntryRepository = guestBookEntryRepositoryMockk
    )

    private val uuid = UUID.randomUUID()

    @Test
    fun `should map guest book to a dto`() = runTest {
        val guestBookEntity = initGuestBook(title = "@home").withId().toEntity()

        every { guestBookRepositoryMockk.findById(uuid) } returns Optional.of(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(uuid) ?: error("missed mocking?")

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() = runTest {
        val anEntry = initGuestBookEntry(
            guestBook = initGuestBook(),
            creatorName = "me",
            entry = "too",
            persistent = Persistent(),
        ).withId().toEntity()

        every { guestBookEntryRepositoryMockk.findById(uuid) } returns Optional.of(anEntry)

        val guestBookEntryModel = guestBookServiceToTest.findEntry(uuid) ?: error("missed mocking?")

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

        every { guestBookRepositoryMockk.save(capture(guestBookEntitySlot)) } returns GuestBookDao(guestBook)

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

        every { guestBookEntryRepositoryMockk.save(capture(guestBookEntryEntitySlot)) } returns GuestBookEntryDao(
            guestBookEntry
        )

        guestBookServiceToTest.saveOrUpdate(guestBookEntry)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBookDao).isNotNull()
            assertThat(guestBookEntryEntity.creatorName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}