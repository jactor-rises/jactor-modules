package com.github.jactor.persistence

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

internal class GuestBookServiceTest {

    private val guestBookRepositoryMockk: GuestBookRepository = mockk {}
    private val guestBookEntryRepositoryMockk: GuestBookEntryRepository = mockk {}

    private val guestBookServiceToTest: GuestBookService = DefaultGuestBookService(
        guestBookRepository = guestBookRepositoryMockk,
        guestBookEntryRepository = guestBookEntryRepositoryMockk
    )

    private val uuid = UUID.randomUUID()

    @Test
    fun `should map guest book to a dto`() {
        val guestBookEntity = GuestBookBuilder.new(
            guestBook = GuestBook(Persistent(), HashSet(), "@home", null)
        ).buildGuestBookEntity()

        every { guestBookRepositoryMockk.findById(uuid) } returns Optional.of(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(uuid) ?: throw mockError()

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() {
        val anEntry = GuestBookBuilder.new().withEntry(
            guestBookEntry = GuestBookEntry(
                guestBook = GuestBook(),
                creatorName = "me",
                entry = "too",
                persistent = Persistent(),
            )
        ).buildGuestBookEntryEntity()

        every { guestBookEntryRepositoryMockk.findById(uuid) } returns Optional.of(anEntry)

        val guestBookEntryModel = guestBookServiceToTest.findEntry(uuid) ?: throw mockError()

        assertAll {
            assertThat(guestBookEntryModel.creatorName).isEqualTo("me")
            assertThat(guestBookEntryModel.entry).isEqualTo("too")
        }
    }

    private fun mockError(): AssertionError {
        return AssertionError("missed mocking?")
    }

    @Test
    fun `should save GuestBookDto as GuestBookEntity`() {
        val guestBookEntry = GuestBookEntry(
            creatorName = "me",
            entry = "all about this",
            guestBook = GuestBook()
        )

        val guestBookEntitySlot = slot<GuestBookEntity>()
        val guestBook = GuestBook(
            entries = setOf(guestBookEntry),
            title = "home sweet home",
            user = initUser()
        )

        every { guestBookRepositoryMockk.save(capture(guestBookEntitySlot)) } returns GuestBookEntity(guestBook)

        guestBookServiceToTest.saveOrUpdate(guestBook)
        val guestBookEntity = guestBookEntitySlot.captured

        assertAll {
            assertThat(guestBookEntity.getEntries()).hasSize(1)
            assertThat(guestBookEntity.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity.user).isNotNull()
        }
    }

    @Test
    fun `should save GuestBookEntryDto as GuestBookEntryEntity`() {
        val guestBookEntryEntitySlot = slot<GuestBookEntryEntity>()
        val guestBookEntry = GuestBookEntry(
            guestBook = GuestBook(),
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every { guestBookEntryRepositoryMockk.save(capture(guestBookEntryEntitySlot)) } returns GuestBookEntryEntity(
            guestBookEntry
        )

        guestBookServiceToTest.saveOrUpdate(guestBookEntry)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBook).isNotNull()
            assertThat(guestBookEntryEntity.creatorName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}