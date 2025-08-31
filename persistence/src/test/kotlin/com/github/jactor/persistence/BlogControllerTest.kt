package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.jactor.persistence.common.Persistent
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
            every { blogServiceSpyk.find(it) } returns Blog()
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
            every { blogServiceSpyk.findEntryBy(it) } returns BlogEntry()
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
        every { blogServiceSpyk.findBlogsBy("Anything") } returns listOf(Blog())

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
            every { blogServiceSpyk.findEntriesForBlog(it) } returns listOf(BlogEntry())
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
        val blog = Blog(
            persistent = Persistent(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blog) } returns blog

        val blogDto = webTestClient
            .put()
            .uri("/blog/${blog.id}")
            .bodyValue(blog.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blogDto).isNotNull()

        verify { blogServiceSpyk.saveOrUpdate(blog) }
    }

    @Test
    fun `should create a blog`() {
        val createdBlog = Blog(
            persistent = Persistent(
                id = UUID.randomUUID()
            )
        )

        every { blogServiceSpyk.saveOrUpdate(blog = any()) } returns createdBlog

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

        verify { blogServiceSpyk.saveOrUpdate(blog = any()) }
    }

    @Test
    fun `should persist changes to existing blog entry`() {
        val blogEntry = BlogEntry(
            persistent = Persistent(id = UUID.randomUUID())
        )

        every { blogServiceSpyk.saveOrUpdate(blogEntry = blogEntry) } returns blogEntry

        val blogEntryDto = webTestClient
            .put()
            .uri("/blog/entry/${blogEntry.id}")
            .bodyValue(blogEntry.toDto())
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntryDto).isNotNull()

        verify { blogServiceSpyk.saveOrUpdate(blogEntry = blogEntry) }
    }

    @Test
    fun `should create blog entry`() {
        val blogEntryDto = BlogEntryDto(
            entry = "hi",
            blogDto = BlogDto(persistentDto = PersistentDto(id = UUID.randomUUID()))
        )

        val blogEntryCreated = BlogEntry(
            persistent = Persistent(id = UUID.randomUUID()),
            entry = blogEntryDto.entry,
            blog = Blog(persistent = Persistent(id = UUID.randomUUID()))
        )

        every { blogServiceSpyk.saveOrUpdate(blogEntry = any()) } returns blogEntryCreated

        val blogEntry = webTestClient
            .post()
            .uri("/blog/entry")
            .bodyValue(blogEntryDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntry?.entry).isEqualTo(blogEntryDto.entry)

        verify { blogServiceSpyk.saveOrUpdate(blogEntry = any()) }
    }
}