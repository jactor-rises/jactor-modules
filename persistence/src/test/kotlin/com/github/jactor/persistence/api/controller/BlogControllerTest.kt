package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.blog.BlogEntryModel
import com.github.jactor.persistence.blog.BlogModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.verify

internal class BlogControllerTest : AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.find(it) } returns BlogModel()
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
            every { blogServiceSpyk.findEntryBy(it) } returns BlogEntryModel()
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
        every { blogServiceSpyk.findBlogsBy(title = "Anything") } returns emptyList()

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/title/Anything"),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BlogDto>>() {}
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(blogResponse.body).isNull()
        }
    }

    @Test
    fun `should find blogs by title`() {
        every { blogServiceSpyk.findBlogsBy("Anything") } returns listOf(BlogModel())

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/title/Anything"),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BlogDto>>() {}
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogResponse.body as List).isNotEmpty()
        }
    }

    @Test
    fun `should not find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntriesForBlog(it) } returns emptyList()
        }

        val blogEntriesResponse = testRestTemplate.exchange(
            buildFullPath("/blog/$uuid/entries"),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BlogEntryDto>>() {}
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
            buildFullPath("/blog/$uuid/entries"),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BlogEntryDto>>() {}
        )

        assertAll {
            assertThat(blogEntriesResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(blogEntriesResponse.body as List).isNotEmpty()
        }
    }

    @Test
    fun `should persist changes to existing blog`() {
        val blogModel = BlogModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blogModel) } returns blogModel

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog/${blogModel.id}"), HttpMethod.PUT, HttpEntity(blogModel.toDto()),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogModel) }
    }

    @Test
    fun `should create a blog`() {
        val createdBlogModel = BlogModel(
            persistentModel = PersistentModel()
        )

        every { blogServiceSpyk.saveOrUpdate(blogModel = any()) } returns createdBlogModel

        val blogResponse = testRestTemplate.exchange(
            buildFullPath("/blog"), HttpMethod.POST, HttpEntity(BlogDto()),
            BlogDto::class.java
        )

        assertAll {
            assertThat(blogResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogResponse).isNotNull()
            assertThat(blogResponse.body?.persistentDto?.id).isEqualTo(createdBlogModel.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogModel = any()) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryModel = BlogEntryModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blogEntryModel = blogEntryModel) } returns blogEntryModel

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry/${blogEntryModel.id}"),
            HttpMethod.PUT, HttpEntity(blogEntryModel.toDto()), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(blogEntryResponse.body).isNotNull()
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryModel = blogEntryModel) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto(
            entry = "hi",
            blogDto = BlogDto(persistentDto = PersistentDto(id = UUID.randomUUID()))
        )

        val blogEntryModelCreated = BlogEntryModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()),
            entry = blogEntryDto.entry,
            blog = BlogModel(persistentModel = PersistentModel(id = UUID.randomUUID()))
        )

        every { blogServiceSpyk.saveOrUpdate(blogEntryModel = any()) } returns blogEntryModelCreated

        val blogEntryResponse = testRestTemplate.exchange(
            buildFullPath("/blog/entry"), HttpMethod.POST, HttpEntity(blogEntryDto), BlogEntryDto::class.java
        )

        assertAll {
            assertThat(blogEntryResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(blogEntryResponse.body).isNotNull()
            assertThat(blogEntryResponse.body?.entry).isEqualTo(blogEntryDto.entry)
            assertThat(blogEntryResponse.body?.persistentDto?.id).isEqualTo(blogEntryModelCreated.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogEntryModel = any()) }
    }
}
