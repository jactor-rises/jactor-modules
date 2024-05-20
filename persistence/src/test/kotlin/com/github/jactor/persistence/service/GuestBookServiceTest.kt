package com.github.jactor.persistence.service

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.GuestBookBuilder
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository
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
            guestBookDto = GuestBookDto(PersistentDto(), HashSet(), "@home", null)
        ).buildGuestBookEntity()

        every { guestBookRepositoryMockk.findById(uuid) } returns Optional.of(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(uuid) ?: throw mockError()

        assertThat(title).isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() {
        val anEntry = GuestBookBuilder.new().withEntry(
            guestBookEntryDto = GuestBookEntryDto(PersistentDto(), GuestBookDto(), "me", "too")
        ).buildGuestBookEntryEntity()

        every { guestBookEntryRepositoryMockk.findById(uuid) } returns Optional.of(anEntry)

        val (_, _, creatorName, entry) = guestBookServiceToTest.findEntry(uuid) ?: throw mockError()

        assertAll {
            assertThat(creatorName).isEqualTo("me")
            assertThat(entry).isEqualTo("too")
        }
    }

    private fun mockError(): AssertionError {
        return AssertionError("missed mocking?")
    }

    @Test
    fun `should save GuestBookDto as GuestBookEntity`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.entry = "all about this"

        val guestBookEntitySlot = slot<GuestBookEntity>()
        val guestBookDto = GuestBookDto()

        guestBookDto.entries = setOf(guestBookEntryDto)
        guestBookDto.title = "home sweet home"
        guestBookDto.userInternal = UserInternalDto()

        every { guestBookRepositoryMockk.save(capture(guestBookEntitySlot)) } returns GuestBookEntity(guestBookDto)

        guestBookServiceToTest.saveOrUpdate(guestBookDto)
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
        val guestBookEntryDto = GuestBookEntryDto()

        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.entry = "if i where a rich man..."

        every { guestBookEntryRepositoryMockk.save(capture(guestBookEntryEntitySlot)) } returns GuestBookEntryEntity(
            guestBookEntryDto
        )

        guestBookServiceToTest.saveOrUpdate(guestBookEntryDto)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll {
            assertThat(guestBookEntryEntity.guestBook).isNotNull()
            assertThat(guestBookEntryEntity.creatorName).isEqualTo("me")
            assertThat(guestBookEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}