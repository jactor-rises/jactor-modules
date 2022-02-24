package com.github.jactor.persistence.service

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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional
import java.util.function.Supplier

@ExtendWith(MockitoExtension::class)
internal class GuestBookServiceTest {

    @InjectMocks
    private lateinit var guestBookServiceToTest: GuestBookService

    @Mock
    private lateinit var guestBookRepositoryMock: GuestBookRepository

    @Mock
    private lateinit var guestBookEntryRepositoryMock: GuestBookEntryRepository

    @Test
    fun `should map guest book to a dto`() {
        val guestBookEntity = Optional.of(aGuestBook(GuestBookDto(PersistentDto(), HashSet(), "@home", null)))
        whenever(guestBookRepositoryMock.findById(1001L)).thenReturn(guestBookEntity)

        val (_, _, title) = guestBookServiceToTest.find(1001L).orElseThrow(mockError())
        assertThat(title).`as`("title").isEqualTo("@home")
    }

    @Test
    fun `should map guest book entry to a dto`() {
        val anEntry = Optional.of(
            aGuestBookEntry(
                GuestBookEntryDto(PersistentDto(), GuestBookDto(), "me", "too")
            )
        )

        whenever(guestBookEntryRepositoryMock.findById(1001L)).thenReturn(anEntry)

        val (_, _, creatorName, entry) = guestBookServiceToTest.findEntry(1001L).orElseThrow(mockError())

        assertAll(
            { assertThat(creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(entry).`as`("entry").isEqualTo("too") }
        )
    }

    private fun mockError(): Supplier<AssertionError> {
        return Supplier { AssertionError("missed mocking?") }
    }

    @Test
    fun `should save GuestBookDto as GuestBookEntity`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.entry = "all about this"

        val guestBookDto = GuestBookDto()

        guestBookDto.entries = setOf(guestBookEntryDto)
        guestBookDto.title = "home sweet home"
        guestBookDto.userInternal = UserInternalDto()

        Mockito.`when`(guestBookRepositoryMock.save(ArgumentMatchers.any())).thenReturn(GuestBookEntity(guestBookDto))

        guestBookServiceToTest.saveOrUpdate(guestBookDto)
        val argCaptor = ArgumentCaptor.forClass(GuestBookEntity::class.java)
        verify(guestBookRepositoryMock).save(argCaptor.capture())
        val guestBookEntity = argCaptor.value

        assertAll(
            { assertThat(guestBookEntity.getEntries()).`as`("entries").hasSize(1) },
            { assertThat(guestBookEntity.title).`as`("title").isEqualTo("home sweet home") },
            { assertThat(guestBookEntity.user).`as`("user").isNotNull() }
        )
    }

    @Test
    fun `should save GuestBookEntryDto as GuestBookEntryEntity`() {
        val guestBookEntryDto = GuestBookEntryDto()

        guestBookEntryDto.guestBook = GuestBookDto()
        guestBookEntryDto.creatorName = "me"
        guestBookEntryDto.entry = "if i where a rich man..."

        Mockito.`when`(guestBookEntryRepositoryMock.save(ArgumentMatchers.any())).thenReturn(GuestBookEntryEntity(guestBookEntryDto))

        guestBookServiceToTest.saveOrUpdate(guestBookEntryDto)
        val argCaptor = ArgumentCaptor.forClass(
            GuestBookEntryEntity::class.java
        )

        verify(guestBookEntryRepositoryMock).save(argCaptor.capture())
        val guestBookEntryEntity = argCaptor.value

        assertAll(
            { assertThat(guestBookEntryEntity.guestBook).`as`("guest book").isNotNull() },
            { assertThat(guestBookEntryEntity.creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(guestBookEntryEntity.entry).`as`("entry").isEqualTo("if i where a rich man...") }
        )
    }
}