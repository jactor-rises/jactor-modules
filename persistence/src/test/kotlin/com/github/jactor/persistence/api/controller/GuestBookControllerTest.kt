package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.GuestBookModel
import com.github.jactor.persistence.dto.GuestBookEntryModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.verify

internal class GuestBookControllerTest : AbstractSpringBootNoDirtyContextTest(){
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
            buildFullPath("/guestBook/$uuid"), GuestBookModel::class.java
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
        every { guestBookServiceSpyk.findEntry(id = uuid) } returns GuestBookEntryModel()

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/$uuid"),
            GuestBookModel::class.java
        )

        assertAll {
            assertThat(guestBookEntryRespnse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(guestBookEntryRespnse.body).isNotNull()
        }
    }

    @Test
    fun `should modify existing guest book`() {
        val guestBookModel = GuestBookModel()
        guestBookModel.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookModel) } returns guestBookModel

        val guestbookResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/${guestBookModel.id}"),
            HttpMethod.PUT, HttpEntity(guestBookModel), GuestBookModel::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.id).isEqualTo(guestBookModel.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookModel) }
        }
    }

    @Test
    fun `should create a guest book`() {
        val guestBookModel = GuestBookModel()
        val createdDto = GuestBookModel()
        createdDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookModel) } returns createdDto

        val guestbookResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook"), guestBookModel,
            GuestBookModel::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.id).isEqualTo(createdDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookModel) }
        }
    }

    @Test
    fun `should modify existing guest book entry`() {
        val guestBookEntryModel = GuestBookEntryModel()
        guestBookEntryModel.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) } returns guestBookEntryModel

        val guestbookEntryResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/entry/${guestBookEntryModel.id}"), HttpMethod.PUT, HttpEntity(guestBookEntryModel),
            GuestBookEntryModel::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.id).isEqualTo(guestBookEntryModel.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) }
        }
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryModel = GuestBookEntryModel()
        val createdDto = GuestBookEntryModel()
        createdDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) } returns createdDto

        val guestbookEntryResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook/entry"), guestBookEntryModel,
            GuestBookEntryModel::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.id).isEqualTo(createdDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryModel) }
        }
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}