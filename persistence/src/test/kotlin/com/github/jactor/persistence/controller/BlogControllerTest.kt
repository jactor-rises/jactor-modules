package com.github.jactor.persistence.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.github.jactor.persistence.JactorPersistence
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.service.BlogService
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.verify

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JactorPersistence::class], webEnvironment = WebEnvironment.RANDOM_PORT)
internal class BlogControllerTest {
    @LocalServerPort
    private val port = 0

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @MockkBean
    private lateinit var blogServiceMock: BlogService

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.find(it) } returns BlogDto()
        }

        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/$uuid"), BlogDto::class.java)

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogResponse).isNotNull()
        }
    }

    @Test
    fun `should not find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.find(it) } returns null
        }

        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/$uuid"), BlogDto::class.java)

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogResponse.body).isNull()
        }
    }

    @Test
    fun `should find a blog entry`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.findEntryBy(it) } returns BlogEntryDto()
        }

        val blogEntryResponse = testRestTemplate.getForEntity(
            buildFullPath("/blog/entry/$uuid"),
            BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogEntryResponse.body).isNotNull()
        }
    }

    @Test
    fun `should not find a blog entry`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.findEntryBy(it) } returns null
        }

        val blogEntryResponse = testRestTemplate.getForEntity(
            buildFullPath("/blog/entry/$uuid"),
            BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogEntryResponse.body).isNull()
        }
    }

    @Test
    fun `should not find blogs by title`() {
        every { blogServiceMock.findBlogsBy("Anything") } returns emptyList()

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs()
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogResponse.body).isNull()
        }
    }

    @Test
    fun `should find blogs by title`() {
        every { blogServiceMock.findBlogsBy("Anything") } returns listOf(BlogDto())

        val blogResponse =
            testRestTemplate.exchange(buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs())

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogResponse.body as List).isNotEmpty()
        }
    }

    private fun typeIsListOfBlogs(): ParameterizedTypeReference<List<BlogDto>> {
        return object : ParameterizedTypeReference<List<BlogDto>>() {}
    }

    @Test
    fun `should not find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.findEntriesForBlog(it) } returns emptyList()
        }

        val blogEntriesResponse = testRestTemplate.exchange(
            buildFullPath("/blog/$uuid/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries()
        )

        assertAll {
            assertThat(blogEntriesResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogEntriesResponse.body).isNull()
        }
    }

    @Test
    fun `should find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceMock.findEntriesForBlog(it) } returns listOf(BlogEntryDto())
        }

        val blogEntriesResponse = testRestTemplate.exchange(
            buildFullPath("/blog/$uuid/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries()
        )

        assertAll {
            assertThat(blogEntriesResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogEntriesResponse.body as List).isNotEmpty()
        }
    }

    private fun typeIsListOfBlogEntries(): ParameterizedTypeReference<List<BlogEntryDto>> {
        return object : ParameterizedTypeReference<List<BlogEntryDto>>() {}
    }

    @Test
    fun `should persist changes to existing blog`() {
        val blogDto = BlogDto()
        blogDto.id = UUID.randomUUID()

        every { blogServiceMock.saveOrUpdate(blogDto) } returns blogDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/${blogDto.id}"), HttpMethod.PUT, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogResponse.body).isNotNull()
        }

        verify { blogServiceMock.saveOrUpdate(blogDto) }
    }

    @Test
    fun `should create a blog`() {
        val blogDto = BlogDto()
        val createdDto = BlogDto()
        createdDto.id = UUID.randomUUID()

        every { blogServiceMock.saveOrUpdate(blogDto) } returns createdDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog"), HttpMethod.POST, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogResponse).isNotNull()
            assertThat(blogResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceMock.saveOrUpdate(blogDto) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.id = UUID.randomUUID()

        every { blogServiceMock.saveOrUpdate(blogEntryDto) } returns blogEntryDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry/${blogEntryDto.id}"),
            HttpMethod.PUT, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogEntryResponse.body).isNotNull()
        }

        verify { blogServiceMock.saveOrUpdate(blogEntryDto) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto()
        val createdDto = BlogEntryDto()
        createdDto.id = UUID.randomUUID()

        every { blogServiceMock.saveOrUpdate(blogEntryDto) } returns createdDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry"), HttpMethod.POST, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogEntryResponse.body).isNotNull()
            assertThat(blogEntryResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceMock.saveOrUpdate(blogEntryDto) }
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}
