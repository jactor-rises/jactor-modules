package com.github.jactor.persistence

import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest

internal class BlogServiceTest {

    private val blogRepositoryMockk: BlogRepository = mockk {}
    private val userServiceMockk: UserService = mockk {}
    private val blogServiceToTest: BlogService = BlogServiceImpl(blogRepository = blogRepositoryMockk)

    private val uuid: UUID = UUID.randomUUID()

    @Test
    fun `should map blog to dto`() = runTest {
        val blogDao = Blog(
            created = null,
            persistent = Persistent(),
            title = "full speed ahead",
            user = null
        ).withId().toBlogDao()

        every { blogRepositoryMockk.findBlogById(id = uuid) } returns blogDao

        val (_, _, title) = blogServiceToTest.find(uuid) ?: throw AssertionError("missed mocking?")

        assertThat(title).isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() = runTest {
        val blogEntry = BlogEntry(
            blog = initBlog(), creatorName = "me", entry = "too",
            persistent = Persistent(),
        )

        val anEntry = blogEntry.withId().toBlogEntryDao()

        every { blogRepositoryMockk.findBlogEntryById(uuid) } returns anEntry

        val (_, creatorName, entry, _) = blogServiceToTest.findEntryBy(blogEntryId = uuid)
            ?: error("no entry found???")

        assertAll {
            assertThat(creatorName).isEqualTo("me")
            assertThat(entry).isEqualTo("too")
        }
    }

    @Test
    fun `should find blogs for title`() = runTest {
        val blogsToFind = listOf(initBlog(title = "Star Wars").withId().toBlogDao())

        every { blogRepositoryMockk.findBlogsByTitle("Star Wars") } returns blogsToFind

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() = runTest {
        val blogEntryEntities: List<BlogEntryDao> = listOf(
            BlogEntry(
                blog = initBlog(),
                creatorName = "you",
                entry = "too",
                persistent = Persistent(),
            ).withId().toBlogEntryDao()
        )

        every { blogRepositoryMockk.findBlogEntriesByBlogId(uuid) } returns blogEntryEntities

        val blogEntries = blogServiceToTest.findEntriesForBlog(uuid)

        assertAll {
            assertThat(blogEntries).hasSize(1)
            assertThat(blogEntries[0].creatorName).isEqualTo("you")
            assertThat(blogEntries[0].entry).isEqualTo("too")
        }
    }

    @Test
    fun `should save BlogDto as BlogEntity`() = runTest {
        val blogEntitySlot = slot<BlogDao>()
        val blog = Blog(
            created = LocalDate.now(),
            title = "some blog",
            user = initUser(username = "itsme")
        )

        coEvery { userServiceMockk.find(username = any()) } returns null
        every { blogRepositoryMockk.save(capture(blogEntitySlot)) } returns BlogDao(blog)

        blogServiceToTest.saveOrUpdate(blog = blog)
        val blogEntity = blogEntitySlot.captured

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("some blog")
            assertThat(blogEntity.user).isNull()
        }
    }

    @Test
    fun `should save BlogEntryDto as BlogEntryEntity`() = runTest {
        val blogEntryEntitySlot = slot<BlogEntryDao>()
        val blogEntry = BlogEntry(
            blog = initBlog(
                persistent = Persistent(id = UUID.randomUUID()),
                user = initUser(username = "itsme"),
            ),
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        coEvery { userServiceMockk.find(username = any()) } returns null
        every { blogRepositoryMockk.save(capture(blogEntryEntitySlot)) } returns blogEntry.toBlogEntryDao()

        blogServiceToTest.saveOrUpdate(blogEntry)
        val blogEntryEntity = blogEntryEntitySlot.captured

        assertAll {
            assertThat(blogEntryEntity.blogDao).isNotNull()
            assertThat(blogEntryEntity.creatorName).isEqualTo("me")
            assertThat(blogEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
