package com.github.jactor.persistence.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.github.jactor.persistence.JactorPersistence
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.service.GuestBookService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JactorPersistence::class], webEnvironment = WebEnvironment.RANDOM_PORT)
internal class GuestBookControllerTest {

    @LocalServerPort
    private val port = 0

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @MockkBean
    private lateinit var guestBookServiceMock: GuestBookService

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should not get a guest book`() {
        every { guestBookServiceMock.find(1L) } returns null

        val guestBookRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/1"), GuestBookDto::class.java)

        assertAll(
            { assertThat(guestBookRespnse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(guestBookRespnse.body).`as`("guest book").isNull() }
        )
    }

    @Test
    fun `should get a guest book`() {
        every { guestBookServiceMock.find(1L) } returns GuestBookDto()

        val guestBookRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/1"), GuestBookDto::class.java)

        assertAll(
            { assertThat(guestBookRespnse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(guestBookRespnse.body).`as`("guest book").isNotNull() }
        )
    }

    @Test
    fun `should not get a guest book entry`() {
        every { guestBookServiceMock.findEntry(1L) } returns null

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/1"),
            GuestBookDto::class.java
        )

        assertAll(
            { assertThat(guestBookEntryRespnse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(guestBookEntryRespnse.body).`as`("guest book entry").isNull() }
        )
    }

    @Test
    fun `should get a guest book entry`() {
        every { guestBookServiceMock.findEntry(1L) } returns GuestBookEntryDto()

        val guestBookEntryRespnse = testRestTemplate.getForEntity(
            buildFullPath("/guestBook/entry/1"),
            GuestBookDto::class.java
        )

        assertAll(
            { assertThat(guestBookEntryRespnse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(guestBookEntryRespnse.body).`as`("guest book entry").isNotNull() }
        )
    }

    @Test
    fun `should modify existing guest book`() {
        val guestBookDto = GuestBookDto()
        guestBookDto.id = 1L

        every { guestBookServiceMock.saveOrUpdate(guestBookDto) } returns guestBookDto

        val guestbookResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/1"), HttpMethod.PUT, HttpEntity(guestBookDto), GuestBookDto::class.java
        )

        assertAll(
            { assertThat(guestbookResponse.statusCode).`as`("status").isEqualTo(HttpStatus.ACCEPTED) },
            { assertThat(guestbookResponse.body).`as`("guest book").isNotNull() },
            { assertThat(guestbookResponse.body?.id).`as`("guest book id").isEqualTo(1L) },
            { verify { guestBookServiceMock.saveOrUpdate(guestBookDto) } }
        )
    }

    @Test
    fun `should create a guest book`() {
        val guestBookDto = GuestBookDto()
        val createdDto = GuestBookDto()
        createdDto.id = 1L

        every { guestBookServiceMock.saveOrUpdate(guestBookDto) } returns createdDto

        val guestbookResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook"), guestBookDto,
            GuestBookDto::class.java
        )

        assertAll(
            { assertThat(guestbookResponse.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(guestbookResponse.body).`as`("guest book").isNotNull() },
            { assertThat(guestbookResponse.body?.id).`as`("guest book id").isEqualTo(1L) },
            { verify { guestBookServiceMock.saveOrUpdate(guestBookDto) } }
        )
    }

    @Test
    fun `should modify existing guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        guestBookEntryDto.id = 1L

        every { guestBookServiceMock.saveOrUpdate(guestBookEntryDto) } returns guestBookEntryDto

        val guestbookEntryResponse = testRestTemplate.exchange(
            buildFullPath("/guestBook/entry/1"), HttpMethod.PUT, HttpEntity(guestBookEntryDto),
            GuestBookEntryDto::class.java
        )

        assertAll(
            { assertThat(guestbookEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.ACCEPTED) },
            { assertThat(guestbookEntryResponse.body).`as`("guest book entry").isNotNull() },
            { assertThat(guestbookEntryResponse.body?.id).isEqualTo(1L) },
            { verify { guestBookServiceMock.saveOrUpdate(guestBookEntryDto) } }
        )
    }

    @Test
    fun `should create a guest book entry`() {
        val guestBookEntryDto = GuestBookEntryDto()
        val createdDto = GuestBookEntryDto()
        createdDto.id = 1L

        every { guestBookServiceMock.saveOrUpdate(guestBookEntryDto) } returns createdDto

        val guestbookEntryResponse = testRestTemplate.postForEntity(
            buildFullPath("/guestBook/entry"), guestBookEntryDto,
            GuestBookEntryDto::class.java
        )

        assertAll(
            { assertThat(guestbookEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(guestbookEntryResponse.body).`as`("guest book entry").isNotNull() },
            { assertThat(guestbookEntryResponse.body?.id).isEqualTo(1L) },
            { verify { guestBookServiceMock.saveOrUpdate(guestBookEntryDto) } }
        )
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}