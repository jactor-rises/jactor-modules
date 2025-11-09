package com.github.jactor.rises.persistence.blog

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.github.jactor.rises.persistence.test.initBlog
import com.github.jactor.rises.persistence.test.initBlogEntry
import com.github.jactor.rises.persistence.test.withId
import com.github.jactor.rises.shared.api.BlogDto
import com.github.jactor.rises.shared.api.BlogEntryDto
import com.github.jactor.rises.shared.api.CreateBlogEntryCommand
import com.github.jactor.rises.shared.api.UpdateBlogTitleCommand
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient

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
        val updateBlogTitleCommand = UpdateBlogTitleCommand(
            blogId = UUID.randomUUID(),
            title = "A new title"
        )

        coEvery { blogServiceMockk.update(updateBlogTitle = any()) } returns initBlog()

        val blogDto = webTestClient
            .put()
            .uri("/blog/${updateBlogTitleCommand.blogId}")
            .bodyValue(updateBlogTitleCommand)
            .exchange()
            .expectStatus().isAccepted
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(blogDto).isNotNull()

        coVerify {
            blogServiceMockk.update(
                updateBlogTitle = withArg {
                    assertThat(it.title).isEqualTo(updateBlogTitleCommand.title)
                }
            )
        }
    }

    @Test
    fun `should create a blog`() {
        val blog = initBlog(
            created = LocalDate.now(),
            title = "Another title",
            userId = UUID.randomUUID(),
        )

        coEvery { blogServiceMockk.saveOrUpdate(blog = any()) } returns blog

        val createdBlog = webTestClient
            .post()
            .uri("/blog")
            .bodyValue(blog.toBlogDto())
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogDto::class.java)
            .returnResult().responseBody

        assertThat(createdBlog).isNotNull().given {
            assertThat(it.persistentDto.id).isEqualTo(blog.id)
        }

        coVerify { blogServiceMockk.saveOrUpdate(blog = any()) }
    }

    @Test
    fun `should create blog entry`() {
        val creatBlogEntryCommand = CreateBlogEntryCommand(
            blogId = UUID.randomUUID(),
            creatorName = "me",
            entry = "hi",
        )

        coEvery { blogServiceMockk.create(createBlogEntry = any()) } answers {
            initBlogEntry(blog = initBlog().withId(), entry = (arg(0) as CreateBlogEntry).entry).withId()
        }

        val blogEntry = webTestClient
            .post()
            .uri("/blog/entry")
            .bodyValue(creatBlogEntryCommand)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BlogEntryDto::class.java)
            .returnResult().responseBody

        assertThat(blogEntry?.entry).isEqualTo(creatBlogEntryCommand.entry)

        coVerify { blogServiceMockk.create(createBlogEntry = any()) }
    }
}
