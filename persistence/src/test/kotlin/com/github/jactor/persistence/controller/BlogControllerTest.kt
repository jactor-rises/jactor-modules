package com.github.jactor.persistence.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.verify

internal class BlogControllerTest: AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.find(it) } returns BlogDto()
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
            every { blogServiceSpyk.find(it) } returns null
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
            every { blogServiceSpyk.findEntryBy(it) } returns BlogEntryDto()
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
            every { blogServiceSpyk.findEntryBy(it) } returns null
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
        every { blogServiceSpyk.findBlogsBy("Anything") } returns emptyList()

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
        every { blogServiceSpyk.findBlogsBy("Anything") } returns listOf(BlogDto())

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
            every { blogServiceSpyk.findEntriesForBlog(it) } returns emptyList()
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
            every { blogServiceSpyk.findEntriesForBlog(it) } returns listOf(BlogEntryDto())
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

        every { blogServiceSpyk.saveOrUpdate(blogDto) } returns blogDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/${blogDto.id}"), HttpMethod.PUT, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogDto) }
    }

    @Test
    fun `should create a blog`() {
        val blogDto = BlogDto()
        val createdDto = BlogDto()
        createdDto.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogDto) } returns createdDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog"), HttpMethod.POST, HttpEntity(blogDto),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogResponse).isNotNull()
            assertThat(blogResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogDto) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogEntryDto) } returns blogEntryDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry/${blogEntryDto.id}"),
            HttpMethod.PUT, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogEntryResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryDto) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto()
        val createdDto = BlogEntryDto()
        createdDto.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogEntryDto) } returns createdDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry"), HttpMethod.POST, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogEntryResponse.body).isNotNull()
            assertThat(blogEntryResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryDto) }
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}
