package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
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
            buildFullPath("/guestBook/$uuid"), GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestBookRespnse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(guestBookRespnse.body).isNull()
        }
    }

    @Test
    fun `should get a guest book`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.find(id = uuid) } returns GuestBookDto()

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
            GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestBookEntryRespnse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(guestBookEntryRespnse.body).isNull()
        }
    }

    @Test
    fun `should get a guest book entry`() {
        val uuid = UUID.randomUUID()
        every { guestBookServiceSpyk.findEntry(id = uuid) } returns GuestBookEntryDto()

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/$uuid"),
            GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestBookEntryRespnse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(guestBookEntryRespnse.body).isNotNull()
        }
    }

    @Test
    fun `should modify existing guest book`() {
        val guestBookDto = GuestBookDto()
        guestBookDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookDto) } returns guestBookDto

        val guestbookResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/${guestBookDto.id}"),
            HttpMethod.PUT, HttpEntity(guestBookDto), GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.id).isEqualTo(guestBookDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookDto) }
        }
    }

    @Test
    fun `should create a guest book`() {
        val guestBookDto = GuestBookDto()
        val createdDto = GuestBookDto()
        createdDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookDto) } returns createdDto

        val guestbookResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook"), guestBookDto,
            GuestBookDto::class.java
        )

        assertAll {
            assertThat(guestbookResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookResponse.body).isNotNull()
            assertThat(guestbookResponse.body?.id).isEqualTo(createdDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookDto) }
        }
    }

    @Test
    fun `should modify existing guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryDto) } returns guestBookEntryDto

        val guestbookEntryResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/entry/${guestBookEntryDto.id}"), HttpMethod.PUT, HttpEntity(guestBookEntryDto),
            GuestBookEntryDto::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.id).isEqualTo(guestBookEntryDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryDto) }
        }
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        val createdDto = GuestBookEntryDto()
        createdDto.id = UUID.randomUUID()

        every { guestBookServiceSpyk.saveOrUpdate(guestBookEntryDto) } returns createdDto

        val guestbookEntryResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook/entry"), guestBookEntryDto,
            GuestBookEntryDto::class.java
        )

        assertAll {
            assertThat(guestbookEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(guestbookEntryResponse.body).isNotNull()
            assertThat(guestbookEntryResponse.body?.id).isEqualTo(createdDto.id)
            verify { guestBookServiceSpyk.saveOrUpdate(guestBookEntryDto) }
        }
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}