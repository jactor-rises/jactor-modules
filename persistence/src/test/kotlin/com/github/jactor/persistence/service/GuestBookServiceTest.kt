package com.github.jactor.persistence.service

import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntity.Companion.aGuestBook
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity.Companion.aGuestBookEntry
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot

@ExtendWith(MockKExtension::class)
internal class GuestBookServiceTest {

    @InjectMockKs
    private lateinit var guestBookServiceToTest: GuestBookService

    @MockK
    private lateinit var guestBookRepositoryMock: GuestBookRepository

    @MockK
    private lateinit var guestBookEntryRepositoryMock: GuestBookEntryRepository

    @Test
    fun `should map guest book to a dto`() {
        val guestBookEntity = aGuestBook(GuestBookDto(PersistentDto(), HashSet(), "@home", null))
        every { guestBookRepositoryMock.findById(1001L) } returns Optional.of(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(1001L) ?: throw mockError()

        assertThat(title).`as`("title").isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() {
        val anEntry = aGuestBookEntry(GuestBookEntryDto(PersistentDto(), GuestBookDto(), "me", "too"))

        every { guestBookEntryRepositoryMock.findById(1001L) } returns Optional.of(anEntry)

        val (_, _, creatorName, entry) = guestBookServiceToTest.findEntry(1001L) ?: throw mockError()

        assertAll(
            { assertThat(creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(entry).`as`("entry").isEqualTo("too") }
        )
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

        every { guestBookRepositoryMock.save(capture(guestBookEntitySlot)) } returns GuestBookEntity(guestBookDto)

        guestBookServiceToTest.saveOrUpdate(guestBookDto)
        val guestBookEntity = guestBookEntitySlot.captured

        assertAll(
            { assertThat(guestBookEntity.getEntries()).`as`("entries").hasSize(1) },
            { assertThat(guestBookEntity.title).`as`("title").isEqualTo("home sweet home") },
            { assertThat(guestBookEntity.user).`as`("user").isNotNull() }
        )
    }

    @Test
    fun `should save GuestBookEntryDto as GuestBookEntryEntity`() {
        val guestBookEntryEntitySlot = slot<GuestBookEntryEntity>()
        val guestBookEntryDto = GuestBookEntryDto()

        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.entry = "if i where a rich man..."

        every { guestBookEntryRepositoryMock.save(capture(guestBookEntryEntitySlot)) } returns GuestBookEntryEntity(
            guestBookEntryDto
        )

        guestBookServiceToTest.saveOrUpdate(guestBookEntryDto)
        val guestBookEntryEntity = guestBookEntryEntitySlot.captured

        assertAll(
            { assertThat(guestBookEntryEntity.guestBook).`as`("guest book").isNotNull() },
            { assertThat(guestBookEntryEntity.creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(guestBookEntryEntity.entry).`as`("entry").isEqualTo("if i where a rich man...") }
        )
    }
}