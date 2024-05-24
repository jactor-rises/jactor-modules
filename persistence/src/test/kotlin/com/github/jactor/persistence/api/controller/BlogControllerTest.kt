package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.BlogModel
import com.github.jactor.persistence.dto.BlogEntryModel
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
            every { blogServiceSpyk.find(it) } returns BlogModel()
        }

        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/$uuid"), BlogModel::class.java)

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

        val blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/$uuid"), BlogModel::class.java)

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogResponse.body).isNull()
        }
    }

    @Test
    fun `should find a blog entry`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntryBy(it) } returns BlogEntryModel()
        }

        val blogEntryResponse = testRestTemplate.getForEntity(
            buildFullPath("/blog/entry/$uuid"),
            BlogEntryModel::class.java
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
            BlogEntryModel::class.java
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
        every { blogServiceSpyk.findBlogsBy("Anything") } returns listOf(BlogModel())

        val blogResponse =
            testRestTemplate.exchange(buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs())

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogResponse.body as List).isNotEmpty()
        }
    }

    private fun typeIsListOfBlogs(): ParameterizedTypeReference<List<BlogModel>> {
        return object : ParameterizedTypeReference<List<BlogModel>>() {}
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
            every { blogServiceSpyk.findEntriesForBlog(it) } returns listOf(BlogEntryModel())
        }

        val blogEntriesResponse = testRestTemplate.exchange(
            buildFullPath("/blog/$uuid/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries()
        )

        assertAll {
            assertThat(blogEntriesResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogEntriesResponse.body as List).isNotEmpty()
        }
    }

    private fun typeIsListOfBlogEntries(): ParameterizedTypeReference<List<BlogEntryModel>> {
        return object : ParameterizedTypeReference<List<BlogEntryModel>>() {}
    }

    @Test
    fun `should persist changes to existing blog`() {
        val blogModel = BlogModel()
        blogModel.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogModel) } returns blogModel

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/${blogModel.id}"), HttpMethod.PUT, HttpEntity(blogModel),
            BlogModel::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogModel) }
    }

    @Test
    fun `should create a blog`() {
        val blogModel = BlogModel()
        val createdDto = BlogModel()
        createdDto.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogModel) } returns createdDto

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog"), HttpMethod.POST, HttpEntity(blogModel),
            BlogModel::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogResponse).isNotNull()
            assertThat(blogResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogModel) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryModel = BlogEntryModel()
        blogEntryModel.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogEntryModel) } returns blogEntryModel

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry/${blogEntryModel.id}"),
            HttpMethod.PUT, HttpEntity(blogEntryModel), BlogEntryModel::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogEntryResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryModel) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryModel = BlogEntryModel()
        val createdDto = BlogEntryModel()
        createdDto.id = UUID.randomUUID()

        every { blogServiceSpyk.saveOrUpdate(blogEntryModel) } returns createdDto

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry"), HttpMethod.POST, HttpEntity(blogEntryModel), BlogEntryModel::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogEntryResponse.body).isNotNull()
            assertThat(blogEntryResponse.body?.id).isEqualTo(createdDto.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryModel) }
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}
