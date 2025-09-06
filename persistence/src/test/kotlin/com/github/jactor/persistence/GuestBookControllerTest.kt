package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initGuestBookEntryEntity
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot

@WebFluxTest(GuestBookController::class)
internal class GuestBookControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @MockkBean private val guestBookServiceMockk: GuestBookService
) {
    @Test
    fun `should not get a guest book`() {
        val uuid = UUID.randomUUID()
        coEvery { guestBookServiceMockk.find(id = uuid) } returns null

        val guestBookExchangeBody = webTestClient.get()
            .uri("/guestBook/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(GuestBook::class.java)
            .returnResult().responseBody

        assertThat(guestBookExchangeBody).isNull()
    }

    @Test
    fun `should get a guest book`() {
        val uuid = UUID.randomUUID()
        coEvery { guestBookServiceMockk.find(id = uuid) } returns initGuestBook()

        val guestBook = webTestClient.get()
            .uri("/guestBook/$uuid")
            .exchange()
            .expectStatus().isOk
            .expectBody(GuestBookDto::class.java)
            .returnResult().responseBody

        assertThat(guestBook).isNotNull()
    }

    @Test
    fun `should not get a guest book entry`() {
        val uuid = UUID.randomUUID()
        coEvery { guestBookServiceMockk.findEntry(id = uuid) } returns null

        val guestBookEntry = webTestClient.get()
            .uri("/guestBook/entry/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(GuestBook::class.java)
            .returnResult().responseBody

        assertThat(guestBookEntry).isNull()
    }

    @Test
    fun `should get a guest book entry`() {
        val uuid = UUID.randomUUID()
        coEvery { guestBookServiceMockk.findEntry(id = uuid) } returns initGuestBookEntryEntity(id = uuid).toModel()

        val guestBookEntry = webTestClient.get()
            .uri("/guestBook/entry/$uuid")
            .exchange()
            .expectStatus().isOk
            .expectBody(GuestBookEntryDto::class.java)
            .returnResult().responseBody

        assertThat(guestBookEntry).isNotNull()
    }

    @Test
    fun `should modify existing guest book`() {
        val uuid = UUID.randomUUID()
        val guestBook = initGuestBook(persistent = Persistent(id = uuid))
        val guestBookSlot = slot<GuestBook>()
        coEvery { guestBookServiceMockk.saveOrUpdate(guestBook = capture(guestBookSlot)) } returns guestBook

        webTestClient.put()
            .uri("/guestBook/update")
            .bodyValue(guestBook.toDto())
            .exchange()
            .expectStatus().isAccepted

        coVerify { guestBookServiceMockk.saveOrUpdate(guestBook = any()) }

        assertThat(guestBookSlot.captured.id).isEqualTo(uuid)
    }

    @Test
    fun `should create a guest book`() {
        val guestBook = GuestBookDto()
        val createdDto = initGuestBook(persistent = Persistent(id = UUID.randomUUID()))

        coEvery { guestBookServiceMockk.saveOrUpdate(guestBook = any()) } returns createdDto

        val guestbook = webTestClient.post()
            .uri("/guestBook")
            .bodyValue(guestBook)
            .exchange()
            .expectStatus().isCreated
            .expectBody(GuestBookDto::class.java)
            .returnResult().responseBody

        assertThat(guestbook?.persistentDto?.id).isEqualTo(createdDto.id)

        coVerify { guestBookServiceMockk.saveOrUpdate(guestBook = any()) }
    }

    @Test
    fun `should modify existing guest book entry`() {
        val uuid = UUID.randomUUID()
        val guestBookEntry = initGuestBookEntry(persistent = Persistent(id = uuid))

        coEvery { guestBookServiceMockk.saveOrUpdate(guestBookEntry) } returns guestBookEntry

        val guestbookEntry = webTestClient.put()
            .uri("/guestBook/entry/update")
            .bodyValue(guestBookEntry.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(GuestBookEntryDto::class.java)
            .returnResult().responseBody

        assertAll {
            assertThat(guestbookEntry?.persistentDto?.id).isEqualTo(guestBookEntry.id)

            coVerify { guestBookServiceMockk.saveOrUpdate(guestBookEntry) }
        }
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        val createdDto = initGuestBookEntry(persistent = Persistent(id = UUID.randomUUID()))

        coEvery { guestBookServiceMockk.saveOrUpdate(guestBookEntry = any()) } returns createdDto

        val guestbookEntry = webTestClient.post()
            .uri("/guestBook/entry")
            .bodyValue(guestBookEntryDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(GuestBookEntryDto::class.java)
            .returnResult().responseBody

        assertAll {
            assertThat(guestbookEntry?.persistentDto?.id).isEqualTo(createdDto.id)

            coVerify { guestBookServiceMockk.saveOrUpdate(guestBookEntry = any()) }
        }
    }
}