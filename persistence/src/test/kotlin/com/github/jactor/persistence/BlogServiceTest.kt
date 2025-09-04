package com.github.jactor.persistence

import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

internal class BlogServiceTest {

    private val blogEntryRepositoryMockk: BlogEntryRepository = mockk {}
    private val blogRepositoryMockk: BlogRepository = mockk {}
    private val userServiceMockk: UserService = mockk {}

    private val blogServiceToTest: BlogService = BlogServiceImpl(
        blogRepository = blogRepositoryMockk,
        blogEntryRepository = blogEntryRepositoryMockk,
        userService = userServiceMockk
    )

    private val uuid: UUID = UUID.randomUUID()

    @Test
    fun `should map blog to dto`() {
        val blogEntity = Blog(
            created = null,
            persistent = Persistent(),
            title = "full speed ahead",
            user = null
        ).withId().toEntity()

        every { blogRepositoryMockk.findById(uuid) } returns Optional.of(blogEntity)

        val (_, _, title) = blogServiceToTest.find(uuid) ?: throw AssertionError("missed mocking?")

        assertThat(title).isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() {
        val blogEntry = BlogEntry(
            blog = initBlog(), creatorName = "me", entry = "too",
            persistent = Persistent(),
        )

        val anEntry = blogEntry.withId().toEntity()

        every { blogEntryRepositoryMockk.findById(uuid) } returns Optional.of(anEntry)

        val (_, creatorName, entry, _) = blogServiceToTest.findEntryBy(uuid)
            ?: throw AssertionError("missed mocking?")

        assertAll {
            assertThat(creatorName).isEqualTo("me")
            assertThat(entry).isEqualTo("too")
        }
    }

    @Test
    fun `should find blogs for title`() {
        val blogsToFind = listOf(initBlog(title = "Star Wars").withId().toEntity())

        every { blogRepositoryMockk.findBlogsByTitle("Star Wars") } returns blogsToFind

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() {
        val blogEntryEntities: List<BlogEntryEntity?> = listOf(
            BlogEntry(
                blog = initBlog(),
                creatorName = "you",
                entry = "too",
                persistent = Persistent(),
            ).withId().toEntity()
        )

        every { blogEntryRepositoryMockk.findByBlogId(uuid) } returns blogEntryEntities

        val blogEntries = blogServiceToTest.findEntriesForBlog(uuid)

        assertAll {
            assertThat(blogEntries).hasSize(1)
            assertThat(blogEntries[0].creatorName).isEqualTo("you")
            assertThat(blogEntries[0].entry).isEqualTo("too")
        }
    }

    @Test
    fun `should save BlogDto as BlogEntity`() {
        val blogEntitySlot = slot<BlogEntity>()
        val blog = Blog(
            created = LocalDate.now(),
            title = "some blog",
            user = initUser(username = "itsme")
        )

        every { userServiceMockk.find(username = any()) } returns null
        every { blogRepositoryMockk.save(capture(blogEntitySlot)) } returns BlogEntity(blog)

        blogServiceToTest.saveOrUpdate(blog = blog)
        val blogEntity = blogEntitySlot.captured

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("some blog")
            assertThat(blogEntity.user).isNull()
        }
    }

    @Test
    fun `should save BlogEntryDto as BlogEntryEntity`() {
        val blogEntryEntitySlot = slot<BlogEntryEntity>()
        val blogEntry = BlogEntry(
            blog = initBlog(
                persistent = Persistent(id = UUID.randomUUID()),
                user = initUser(username = "itsme"),
            ),
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every { userServiceMockk.find(username = any()) } returns null
        every { blogEntryRepositoryMockk.save(capture(blogEntryEntitySlot)) } returns BlogEntryEntity(blogEntry)

        blogServiceToTest.saveOrUpdate(blogEntry)
        val blogEntryEntity = blogEntryEntitySlot.captured

        assertAll {
            assertThat(blogEntryEntity.blog).isNotNull()
            assertThat(blogEntryEntity.creatorName).isEqualTo("me")
            assertThat(blogEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
