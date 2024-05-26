package com.github.jactor.persistence.guestbook

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.user.UserModel
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
            guestBookModel = GuestBookModel(PersistentModel(), HashSet(), "@home", null)
        ).buildGuestBookEntity()

        every { guestBookRepositoryMockk.findById(uuid) } returns Optional.of(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(uuid) ?: throw mockError()

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() {
        val anEntry = GuestBookBuilder.new().withEntry(
            guestBookEntryModel = GuestBookEntryModel(
                guestBook = GuestBookModel(),
                creatorName = "me",
                entry = "too",
                persistentModel = PersistentModel(),
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
        val guestBookEntryModel = GuestBookEntryModel(
            creatorName = "me",
            entry = "all about this",
            guestBook = GuestBookModel()
        )

        val guestBookEntitySlot = slot<GuestBookEntity>()
        val guestBookModel = GuestBookModel(
            entries = setOf(guestBookEntryModel),
            title = "home sweet home",
            user = UserModel()
        )

        every { guestBookRepositoryMockk.save(capture(guestBookEntitySlot)) } returns GuestBookEntity(guestBookModel)

        guestBookServiceToTest.saveOrUpdate(guestBookModel)
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
        val guestBookEntryModel = GuestBookEntryModel(
            guestBook = GuestBookModel(),
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every { guestBookEntryRepositoryMockk.save(capture(guestBookEntryEntitySlot)) } returns GuestBookEntryEntity(
            guestBookEntryModel
        )

        guestBookServiceToTest.saveOrUpdate(guestBookEntryModel)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBook).isNotNull()
            assertThat(guestBookEntryEntity.creatorName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}