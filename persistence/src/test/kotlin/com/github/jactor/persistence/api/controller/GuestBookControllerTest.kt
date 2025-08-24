package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.guestbook.GuestBookModel
import com.github.jactor.persistence.guestbook.GuestBookEntryModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.guestbook.GuestBookService
import com.github.jactor.persistence.test.initGuestBookEntryEntity
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.ninjasquad.springmockk.SpykBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.slot
import io.mockk.verify

@Disabled("wip")
internal class GuestBookControllerTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @SpykBean
    private lateinit var guestBookServiceSpyk: GuestBookService

    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should not get a guest book`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.find(id = uuid) } returns null

        val guestBookRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/$uuid"), GuestBookModel::class.java
        )

        assertAll {
            assertThat(guestBookRespnse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(guestBookRespnse.body).isNull()
        }
    }

    @Test
    fun `should get a guest book`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.find(id = uuid) } returns GuestBookModel()

        val guestBookRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/$uuid"), GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestBookRespnse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(guestBookRespnse.body).isNotNull()
        }
    }

    @Test
    fun `should not get a guest book entry`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.findEntry(id = uuid) } returns null

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/$uuid"),
            GuestBookModel::class.java
        )

        assertAll {
            assertThat(guestBookEntryRespnse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(guestBookEntryRespnse.body).isNull()
        }
    }

    @Test
    fun `should get a guest book entry`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.findEntry(id = uuid) } returns initGuestBookEntryEntity(
            id = uuid
        ).toModel()

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/$uuid"), GuestBookEntryDto::class.java
        )

        assertAll {
            assertThat(guestBookEntryRespnse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(guestBookEntryRespnse.body).isNotNull()
        }
    }

    @Test
    fun `should modify existing guest book`() {
        val uuid = UUID.randomUUID()
        val guestBookModel = GuestBookModel(
            persistentModel = PersistentModel(id = uuid)
        )

        val guestBookModelSlot = slot<GuestBookModel>()
        every { guestBookServiceSpyk.saveOrUpdate(guestBookModel = capture(guestBookModelSlot)) } returns guestBookModel

        val guestbookResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/update"),
            HttpMethod.PUT, HttpEntity(guestBookModel.toDto()), GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.persistentDto?.id).isEqualTo(uuid)
        }

        verify { guestBookServiceSpyk.saveOrUpdate(guestBookModel = any()) }
        assertThat(guestBookModelSlot.captured.id).isEqualTo(uuid)
    }

    @Test
    fun `should create a guest book`() {
        val guestBookModel = GuestBookDto()
        val createdDto = GuestBookModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { guestBookServiceSpyk.saveOrUpdate(guestBookModel = any()) } returns createdDto

        val guestbookResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook"), guestBookModel,
            GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.persistentDto?.id).isEqualTo(createdDto.id)
        }

        verify { guestBookServiceSpyk.saveOrUpdate(guestBookModel = any()) }
    }

    @Test
    fun `should modify existing guest book entry`() {
        val uuid = UUID.randomUUID()
        val guestBookEntryModel = GuestBookEntryModel(
            persistentModel = PersistentModel(id = uuid)
        )

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) } returns guestBookEntryModel

        val guestbookEntryResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/entry/update"),
            HttpMethod.PUT,
            HttpEntity(guestBookEntryModel.toDto()),
            GuestBookEntryDto::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.persistentDto?.id).isEqualTo(guestBookEntryModel.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) }
        }
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        val createdDto = GuestBookEntryModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel = any()) } returns createdDto

        val guestbookEntryResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook/entry"), guestBookEntryDto,
            GuestBookEntryDto::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.persistentDto?.id).isEqualTo(createdDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel = any()) }
        }
    }
}