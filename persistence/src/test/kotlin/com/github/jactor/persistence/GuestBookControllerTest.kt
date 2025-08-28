package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.test.initGuestBookEntryEntity
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.slot
import io.mockk.verify

@WebFluxTest(GuestBookController::class)
internal class GuestBookControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @MockkBean private val guestBookServiceMockk: GuestBookService
) {
    @Test
    fun `should not get a guest book`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceMockk.find(id = uuid) } returns null

        val guestBookExchangeBody = webTestClient.get()
            .uri("/guestBook/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(GuestBookModel::class.java)
            .returnResult().responseBody

        assertThat(guestBookExchangeBody).isNull()
    }

    @Test
    fun `should get a guest book`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceMockk.find(id = uuid) } returns GuestBookModel()

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
        every { guestBookServiceMockk.findEntry(id = uuid) } returns null

        val guestBookEntry = webTestClient.get()
            .uri("/guestBook/entry/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(GuestBookModel::class.java)
            .returnResult().responseBody

        assertThat(guestBookEntry).isNull()
    }

    @Test
    fun `should get a guest book entry`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceMockk.findEntry(id = uuid) } returns initGuestBookEntryEntity(id = uuid).toModel()

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
        val guestBookModel = GuestBookModel(
            persistentModel = PersistentModel(id = uuid)
        )

        val guestBookModelSlot = slot<GuestBookModel>()
        every { guestBookServiceMockk.saveOrUpdate(guestBookModel = capture(guestBookModelSlot)) } returns guestBookModel

        webTestClient.put()
            .uri("/guestBook/update")
            .bodyValue(guestBookModel.toDto())
            .exchange()
            .expectStatus().isAccepted

        verify { guestBookServiceMockk.saveOrUpdate(guestBookModel = any()) }

        assertThat(guestBookModelSlot.captured.id).isEqualTo(uuid)
    }

    @Test
    fun `should create a guest book`() {
        val guestBookModel = GuestBookDto()
        val createdDto = GuestBookModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { guestBookServiceMockk.saveOrUpdate(guestBookModel = any()) } returns createdDto

        val guestbook = webTestClient.post()
            .uri("/guestBook")
            .bodyValue(guestBookModel)
            .exchange()
            .expectStatus().isCreated
            .expectBody(GuestBookDto::class.java)
            .returnResult().responseBody

        assertThat(guestbook?.persistentDto?.id).isEqualTo(createdDto.id)

        verify { guestBookServiceMockk.saveOrUpdate(guestBookModel = any()) }
    }

    @Test
    fun `should modify existing guest book entry`() {
        val uuid = UUID.randomUUID()
        val guestBookEntryModel = GuestBookEntryModel(
            persistentModel = PersistentModel(id = uuid)
        )

        every { guestBookServiceMockk.saveOrUpdate(guestBookEntryModel) } returns guestBookEntryModel

        val guestbookEntry = webTestClient.put()
            .uri("/guestBook/entry/update")
            .bodyValue(guestBookEntryModel.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(GuestBookEntryDto::class.java)
            .returnResult().responseBody

        assertAll {
            assertThat(guestbookEntry?.persistentDto?.id).isEqualTo(guestBookEntryModel.id)

            verify { guestBookServiceMockk.saveOrUpdate(guestBookEntryModel) }
        }
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        val createdDto = GuestBookEntryModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { guestBookServiceMockk.saveOrUpdate(guestBookEntryModel = any()) } returns createdDto

        val guestbookEntry = webTestClient.post()
            .uri("/guestBook/entry")
            .bodyValue(guestBookEntryDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(GuestBookEntryDto::class.java)
            .returnResult().responseBody

        assertAll {
            assertThat(guestbookEntry?.persistentDto?.id).isEqualTo(createdDto.id)

            verify { guestBookServiceMockk.saveOrUpdate(guestBookEntryModel = any()) }
        }
    }
}