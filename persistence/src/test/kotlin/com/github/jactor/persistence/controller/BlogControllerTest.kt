package com.github.jactor.persistence.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
        every { blogServiceMock.find(1L) } returns BlogDto()
        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/1"), BlogDto::class.java)

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(blogResponse).`as`("blog").isNotNull() }
        )
    }

    @Test
    fun `should not find a blog`() {
        every { blogServiceMock.find(1L) } returns null

        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/1"), BlogDto::class.java)

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(blogResponse.body).`as`("blog").isNull() }
        )
    }

    @Test
    fun `should find a blog entry`() {
        every { blogServiceMock.findEntryBy(1L) } returns BlogEntryDto()

        val blogEntryResponse = testRestTemplate.getForEntity(
            buildFullPath("/blog/entry/1"),
            BlogEntryDto::class.java
        )

        assertAll(
            { assertThat(blogEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(blogEntryResponse.body).`as`("blog entry").isNotNull() }
        )
    }

    @Test
    fun `should not find a blog entry`() {
        every { blogServiceMock.findEntryBy(1L) } returns null

        val blogEntryResponse = testRestTemplate.getForEntity(
            buildFullPath("/blog/entry/1"),
            BlogEntryDto::class.java
        )

        assertAll(
            { assertThat(blogEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(blogEntryResponse.body).`as`("blog entry").isNull() }
        )
    }

    @Test
    fun `should not find blogs by title`() {
        every { blogServiceMock.findBlogsBy("Anything") } returns emptyList()

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs()
        )

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(blogResponse.body).`as`("blogs").isNull() }
        )
    }

    @Test
    fun `hould find blogs by title`() {
        every { blogServiceMock.findBlogsBy("Anything") } returns listOf(BlogDto())

        val blogResponse =
            testRestTemplate.exchange(buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs())

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(blogResponse.body).`as`("blogs").isNotEmpty() }
        )
    }

    private fun typeIsListOfBlogs(): ParameterizedTypeReference<List<BlogDto>> {
        return object : ParameterizedTypeReference<List<BlogDto>>() {}
    }

    @Test
    fun `should not find blog entries by blog id`() {
        every { blogServiceMock.findEntriesForBlog(1L) } returns emptyList()

        val blogEntriesResponse =
            testRestTemplate.exchange(buildFullPath("/blog/1/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries())

        assertAll(
            { assertThat(blogEntriesResponse.statusCode).`as`("status").isEqualTo(HttpStatus.NO_CONTENT) },
            { assertThat(blogEntriesResponse.body).`as`("blogs").isNull() }
        )
    }

    @Test
    fun `should find blog entries by blog id`() {
        every { blogServiceMock.findEntriesForBlog(1L) } returns listOf(BlogEntryDto())

        val blogEntriesResponse = testRestTemplate.exchange(
            buildFullPath("/blog/1/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries()
        )

        assertAll(
            { assertThat(blogEntriesResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(blogEntriesResponse.body).`as`("blogs").isNotEmpty() }
        )
    }

    private fun typeIsListOfBlogEntries(): ParameterizedTypeReference<List<BlogEntryDto>> {
        return object : ParameterizedTypeReference<List<BlogEntryDto>>() {}
    }

    @Test
    fun `should persist changes to existing blog`() {
        val blogDto = BlogDto()
        blogDto.id = 1L

        every { blogServiceMock.saveOrUpdate(blogDto) } returns blogDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/1"), HttpMethod.PUT, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.ACCEPTED) },
            { assertThat(blogResponse.body).`as`("updated blog").isNotNull() },
            { verify { blogServiceMock.saveOrUpdate(blogDto) } }
        )
    }

    @Test
    fun `should create a blog`() {
        val blogDto = BlogDto()
        val createdDto = BlogDto()
        createdDto.id = 1L

        every { blogServiceMock.saveOrUpdate(blogDto) } returns createdDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog"), HttpMethod.POST, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll(
            { assertThat(blogResponse.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(blogResponse).`as`("created blog").isNotNull() },
            { assertThat(blogResponse.body?.id).`as`("blog id").isEqualTo(1L) },
            { verify { blogServiceMock.saveOrUpdate(blogDto) } }
        )
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.id = 1L

        every { blogServiceMock.saveOrUpdate(blogEntryDto) } returns blogEntryDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry/1"), HttpMethod.PUT, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll(
            { assertThat(blogEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.ACCEPTED) },
            { assertThat(blogEntryResponse.body).`as`("updated entry").isNotNull() },
            { verify { blogServiceMock.saveOrUpdate(blogEntryDto) } }
        )
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto()
        val createdDto = BlogEntryDto()
        createdDto.id = 1L

        every { blogServiceMock.saveOrUpdate(blogEntryDto) } returns createdDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry"), HttpMethod.POST, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll(
            { assertThat(blogEntryResponse.statusCode).`as`("status").isEqualTo(HttpStatus.CREATED) },
            { assertThat(blogEntryResponse.body).`as`("created entry").isNotNull() },
            { assertThat(blogEntryResponse.body?.id).`as`("blog entry id").isEqualTo(1L) },
            { verify { blogServiceMock.saveOrUpdate(blogEntryDto) } }
        )
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}
