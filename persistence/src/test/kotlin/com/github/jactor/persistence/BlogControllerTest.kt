package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.coEvery
import io.mockk.coVerify

@WebFluxTest(BlogController::class)
internal class BlogControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    @MockkBean private val blogServiceMockk: BlogService,
) {
    @Test
    fun `should find a blog`() {
        val uuid = UUID.randomUUID().also {
            coEvery { blogServiceMockk.find(it) } returns initBlog()
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
            coEvery { blogServiceMockk.find(id = it) } returns null
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
            coEvery { blogServiceMockk.findEntryBy(it) } returns initBlogEntry()
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
            coEvery { blogServiceMockk.findEntryBy(it) } returns null
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
        coEvery { blogServiceMockk.findBlogsBy(title = "Anything") } returns emptyList()

        webTestClient
            .get()
            .uri("/blog/title/Anything")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should find blogs by title`() {
        coEvery { blogServiceMockk.findBlogsBy("Anything") } returns listOf(initBlog())

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
            coEvery { blogServiceMockk.findEntriesForBlog(blogId = it) } returns emptyList()
        }

        webTestClient
            .get()
            .uri("/blog/$uuid/entries")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should find blog entries by blog id`() {
        val uuid = UUID.randomUUID().also {
            coEvery { blogServiceMockk.findEntriesForBlog(it) } returns listOf(initBlogEntry())
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
        val blog = initBlog(
            persistent = Persistent(id = UUID.randomUUID())
        )

        coEvery { blogServiceMockk.saveOrUpdate(blog) } returns blog

        val blogDto = webTestClient
            .put()
            .uri("/blog/${blog.id}")
            .bodyValue(blog.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blogDto).isNotNull()

        coVerify { blogServiceMockk.saveOrUpdate(blog) }
    }

    @Test
    fun `should create a blog`() {
        val createdBlog = initBlog().withId()

        coEvery { blogServiceMockk.saveOrUpdate(blog = any()) } returns createdBlog

        val blog = webTestClient
            .post()
            .uri("/blog")
            .bodyValue(BlogDto())
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blog).isNotNull().given {
            assertThat(it.persistentDto.id).isEqualTo(createdBlog.id)
        }

        coVerify { blogServiceMockk.saveOrUpdate(blog = any()) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntry = initBlogEntry(
            persistent = Persistent(id = UUID.randomUUID())
        )

        coEvery { blogServiceMockk.saveOrUpdate(blogEntry = blogEntry) } returns blogEntry

        val blogEntryDto = webTestClient
            .put()
            .uri("/blog/entry/${blogEntry.id}")
            .bodyValue(blogEntry.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntryDto).isNotNull()

        coVerify { blogServiceMockk.saveOrUpdate(blogEntry = blogEntry) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto(
            entry = "hi",
            blogDto = BlogDto(persistentDto = PersistentDto(id = UUID.randomUUID()))
        )

        val blogEntryCreated = initBlogEntry(
            persistent = Persistent(id = UUID.randomUUID()),
            entry = blogEntryDto.entry,
            blog = initBlog(persistent = Persistent(id = UUID.randomUUID()))
        )

        coEvery { blogServiceMockk.saveOrUpdate(blogEntry = any()) } returns blogEntryCreated

        val blogEntry = webTestClient
            .post()
            .uri("/blog/entry")
            .bodyValue(blogEntryDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntry?.entry).isEqualTo(blogEntryDto.entry)

        coVerify { blogServiceMockk.saveOrUpdate(blogEntry = any()) }
    }
}