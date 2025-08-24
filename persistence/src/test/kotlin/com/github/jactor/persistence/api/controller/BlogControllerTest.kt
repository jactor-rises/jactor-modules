package com.github.jactor.persistence.api.controller

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.blog.BlogEntryModel
import com.github.jactor.persistence.blog.BlogModel
import com.github.jactor.persistence.blog.BlogService
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.verify

@WebFluxTest(BlogController::class)
internal class BlogControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @MockkBean private val blogServiceSpyk: BlogService,
) {
    @Test
    fun `should find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.find(it) } returns BlogModel()
        }

        val blogResponse = webTestClient
            .get()
            .uri("/blog/$uuid")
            .exchange()
            .expectStatus().isOk
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blogResponse).isNotNull()
    }

    @Test
    fun `should not find a blog`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.find(id = it) } returns null
        }

        val blogResponse = webTestClient
            .get()
            .uri("/blog/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blogResponse).isNull()
    }

    @Test
    fun `should find a blog entry`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntryBy(it) } returns BlogEntryModel()
        }

        val blogEntryDto = webTestClient
            .get()
            .uri("/blog/entry/$uuid")
            .exchange()
            .expectStatus().isOk
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

            assertThat(blogEntryDto).isNotNull()
    }

    @Test
    fun `should not find a blog entry`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntryBy(it) } returns null
        }

        val result = webTestClient
            .get()
            .uri("/blog/entry/$uuid")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(result).isNull()
    }

    @Test
    fun `should not find blogs by title`() {
        every { blogServiceSpyk.findBlogsBy(title = "Anything") } returns emptyList()

        val blogs = webTestClient
            .get()
            .uri("/blog/title/Anything")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(object : ParameterizedTypeReference<List<BlogDto>>() {})
            .returnResult().responseBody

        assertThat(blogs).isNotNull().isEmpty()
    }

    @Test
    fun `should find blogs by title`() {
        every { blogServiceSpyk.findBlogsBy("Anything") } returns listOf(BlogModel())

        val blogs = webTestClient
            .get()
            .uri("/blog/title/Anything")
            .exchange()
            .expectStatus().isOk
            .expectBody(object : ParameterizedTypeReference<List<BlogDto>>() {})
            .returnResult().responseBody

        assertThat(blogs).isNotNull().hasSize(1)
    }

    @Test
    fun `should not find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntriesForBlog(blogId = it) } returns emptyList()
        }

        val blogEntries = webTestClient
            .get()
            .uri("/blog/$uuid/entries")
            .exchange()
            .expectStatus().isNoContent
            .expectBody(object : ParameterizedTypeReference<List<BlogEntryDto>>() {})
            .returnResult().responseBody

        assertThat(blogEntries).isNotNull().isEmpty()
    }

    @Test
    fun `should find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            every { blogServiceSpyk.findEntriesForBlog(it) } returns listOf(BlogEntryModel())
        }

        val blogEntries = webTestClient
            .get()
            .uri("/blog/$uuid/entries")
            .exchange()
            .expectStatus().isOk
            .expectBody(object : ParameterizedTypeReference<List<BlogEntryDto>>() {})
            .returnResult().responseBody

        assertThat(blogEntries).isNotNull().isNotEmpty()
    }

    @Test
    fun `should persist changes to existing blog`() {
        val blogModel = BlogModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blogModel) } returns blogModel

        val blog = webTestClient
            .put()
            .uri("/blog/${blogModel.id}")
            .bodyValue(blogModel.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blog).isNotNull().isNotNull()

        verify { blogServiceSpyk.saveOrUpdate(blogModel) }
    }

    @Test
    fun `should create a blog`() {
        val createdBlogModel = BlogModel(
            persistentModel = PersistentModel(
                id = UUID.randomUUID()
            )
        )

        every { blogServiceSpyk.saveOrUpdate(blogModel = any()) } returns createdBlogModel

        val blog = webTestClient
            .post()
            .uri("/blog")
            .bodyValue(BlogDto())
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blog).isNotNull().given {
            assertThat(it.persistentDto.id).isEqualTo(createdBlogModel.id)
        }

        verify { blogServiceSpyk.saveOrUpdate(blogModel = any()) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntryModel = BlogEntryModel(
            persistentModel = PersistentModel(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blogEntryModel = blogEntryModel) } returns blogEntryModel

        val blogEntry = webTestClient
            .put()
            .uri("/blog/entry/${blogEntryModel.id}")
            .bodyValue(blogEntryModel.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntry).isNotNull()

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

        val blogEntry = webTestClient
            .post()
            .uri("/blog/entry")
            .bodyValue(blogEntryDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntry?.entry).isEqualTo(blogEntryDto.entry)

        verify { blogServiceSpyk.saveOrUpdate(blogEntryModel = any()) }
    }
}
