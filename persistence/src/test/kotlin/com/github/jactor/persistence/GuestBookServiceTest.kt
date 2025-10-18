package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest

internal class GuestBookServiceTest {
    private val guestBookRepositoryMockk: GuestBookRepository = mockk {}
    private val userRepositoryMockk: UserRepository = mockk {}
    private val guestBookServiceToTest: GuestBookService = GuestBookServiceBean(
        guestBookRepository = guestBookRepositoryMockk
    ).also {
        JactorPersistenceRepositiesConfig.fetchUserRelation = { id -> userRepositoryMockk.findById(id = id) }
        JactorPersistenceRepositiesConfig.fetchGuestBookRelation = { id ->
            guestBookRepositoryMockk.findGuestBookById(id = id)
        }
    }

    private val uuid = UUID.randomUUID()

    @Test
    fun `should map guest book to a dto`() = runTest {
        val guestBookDao = initGuestBook(title = "@home").withId().toGuestBookDao().apply {
            userId = UUID.randomUUID()
        }

        every { guestBookRepositoryMockk.findGuestBookById(id = uuid) } returns guestBookDao
        every { userRepositoryMockk.findById(any()) } returns initUser().withId().toUserDao()

        val (_, _, title) = guestBookServiceToTest.findGuestBook(id = uuid) ?: fail { "missed mocking?" }

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() = runTest {
        val anEntry = initGuestBookEntry(
            guestBook = initGuestBook(title = "guest who?").withId(),
            creatorName = "me",
            entry = "too",
            persistent = Persistent(id = uuid),
        ).toGuestBookEntryDao()

        every { guestBookRepositoryMockk.findGuestBookById(id = any()) } returns mockk(relaxed = true)
        every { guestBookRepositoryMockk.findGuestBookEntryById(id = uuid) } returns anEntry

        val guestBookEntryModel = guestBookServiceToTest.findEntry(id = uuid) ?: fail { "missed mocking?" }

        assertAll {
            assertThat(guestBookEntryModel.guestName).isEqualTo("me")
            assertThat(guestBookEntryModel.entry).isEqualTo("too")
        }
    }

    @Test
    fun `should save GuestBookDto as GuestBookEntity`() = runTest {
        val guestBookEntitySlot = slot<GuestBookDao>()
        val guestBook = initGuestBook(
            persistent = Persistent(id = uuid),
            title = "home sweet home",
            user = initUser().withId()
        )

        every { userRepositoryMockk.findById(id = guestBook.user?.id!!) } returns guestBook.user!!.toUserDao()
        every {
            guestBookRepositoryMockk.save(guestBookDao = capture(guestBookEntitySlot))
        } returns guestBook.toGuestBookDao()

        guestBookServiceToTest.saveOrUpdate(guestBook)
        val guestBookEntity = guestBookEntitySlot.captured

        assertAll {
            assertThat(guestBookEntity.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity.user).isNotNull()
        }
    }

    @Test
    fun `should save GuestBookEntryDto as GuestBookEntryEntity`() = runTest {
        val guestBookEntryEntitySlot = slot<GuestBookEntryDao>()
        val guestBook = initGuestBook(title = "guest who?", user = initUser().withId()).withId()
        val guestBookEntry = initGuestBookEntry(
            guestBook = guestBook,
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every { userRepositoryMockk.findById(id = guestBook.user?.id!!) } returns guestBook.user!!.toUserDao()
        every { guestBookRepositoryMockk.findGuestBookById(id = guestBookEntry.guestBook?.id!!) } returns guestBook
            .toGuestBookDao()

        every {
            guestBookRepositoryMockk.save(guestBookEntryDao = capture(guestBookEntryEntitySlot))
        } returns guestBookEntry.toGuestBookEntryDao()

        guestBookServiceToTest.saveOrUpdate(guestBookEntry)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBookDao).isNotNull()
            assertThat(guestBookEntryEntity.guestName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
