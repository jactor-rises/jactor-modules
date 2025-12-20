package com.github.jactor.rises.persistence.blog

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.config.JactorPersistenceRepositiesConfig
import com.github.jactor.rises.persistence.test.initBlog
import com.github.jactor.rises.persistence.test.initBlogEntry
import com.github.jactor.rises.persistence.test.initUser
import com.github.jactor.rises.persistence.test.withId
import com.github.jactor.rises.persistence.test.withPersistedData
import com.github.jactor.rises.persistence.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.time.LocalDate
import java.util.UUID

internal class BlogServiceTest {
    private val blogRepositoryMockk: BlogRepository = mockk {}
    private val userRepositoryMockk: UserRepository = mockk {}
    private val blogServiceToTest: BlogService = BlogServiceImpl(blogRepository = blogRepositoryMockk).also {
        JactorPersistenceRepositiesConfig.Companion.fetchBlogRelation =
            { id -> blogRepositoryMockk.findBlogById(id = id) }
        JactorPersistenceRepositiesConfig.Companion.fetchUserRelation = { id -> userRepositoryMockk.findById(id = id) }
    }

    private val uuid: UUID = UUID.randomUUID()

    @Test
    fun `should map blog to dto`() = runTest {
        val blogDao = mockk<BlogDao> {
            every { toBlog() } returns Blog(
                created = null,
                persistent = Persistent(),
                title = "full speed ahead",
                userId = null,
            ).withId()
        }

        every { blogRepositoryMockk.findBlogById(id = uuid) } returns blogDao

        val (_, _, title) = blogServiceToTest.find(uuid) ?: throw AssertionError("missed mocking?")

        assertThat(title).isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() = runTest {
        val blogEntryDao = mockk<BlogEntryDao> {
            every { toBlogEntry() } returns initBlogEntry(
                blog = initBlog().withId(),
                creatorName = "me",
                entry = "too",
                persistent = Persistent().withPersistedData(),
            )
        }

        every { blogRepositoryMockk.findBlogEntryById(uuid) } returns blogEntryDao

        val (_, creatorName, entry, _) = blogServiceToTest.findEntryBy(blogEntryId = uuid)
            ?: fail { "no entry found???" }

        assertAll {
            assertThat(creatorName).isEqualTo("me")
            assertThat(entry).isEqualTo("too")
        }
    }

    @Test
    fun `should find blogs for title`() = runTest {
        val blogsToFind = listOf(
            mockk<BlogDao> {
                every { toBlog() } returns initBlog(
                    title = "Star Wars",
                )
            },
        )

        every { blogRepositoryMockk.findBlogsByTitle("Star Wars") } returns blogsToFind

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() = runTest {
        val blogEntryEntities: List<BlogEntryDao> = listOf(
            mockk {
                every { toBlogEntry() } returns BlogEntry(
                    blogId = UUID.randomUUID(),
                    creatorName = "you",
                    entry = "too",
                    persistent = Persistent().withPersistedData(),
                )
            },
        )

        every { blogRepositoryMockk.findBlogEntriesByBlogId(uuid) } returns blogEntryEntities

        val blogEntries = blogServiceToTest.findEntriesForBlog(uuid)

        assertAll {
            assertThat(blogEntries).hasSize(1)
            assertThat(blogEntries.firstOrNull()?.creatorName).isEqualTo("you")
            assertThat(blogEntries.firstOrNull()?.entry).isEqualTo("too")
        }
    }

    @Test
    fun `should save BlogDto as BlogDao`() = runTest {
        val blogDaoSlot = slot<BlogDao>()
        val owner = initUser(username = "itsme").withId()
        val blog = initBlog(
            created = LocalDate.now(),
            title = "some blog",
            userId = owner.id,
        )

        val blogDaoMockk = mockk<BlogDao>(relaxed = true) {
            every { toBlog() } returns blog
        }

        every { userRepositoryMockk.findById(id = blog.userId!!) } returns owner.toUserDao()
        every { blogRepositoryMockk.save(capture(blogDaoSlot)) } returns blogDaoMockk

        blogServiceToTest.saveOrUpdate(blog = blog)
        val blogDao = blogDaoSlot.captured

        assertAll {
            assertThat(blogDao.created).isEqualTo(LocalDate.now())
            assertThat(blogDao.title).isEqualTo("some blog")
            assertThat(blogDao.userId).isEqualTo(owner.id)
        }
    }

    @Test
    fun `should save BlogEntryDto as blogEntryDao`() = runTest {
        val blogEntryDaoSlot = slot<BlogEntryDao>()
        val user = initUser(username = "itsme").withId()
        val owner = initBlog(
            persistent = Persistent(id = UUID.randomUUID()),
            userId = user.id,
        ).withId()

        val blogEntry = BlogEntry(
            blogId = owner.id!!,
            creatorName = "me",
            entry = "if i where a rich man...",
        )

        every { blogRepositoryMockk.findBlogById(id = blogEntry.blogId) } returns owner.toBlogDao()
        every { userRepositoryMockk.findById(id = blogEntry.blogId) } returns user.toUserDao()
        every { blogRepositoryMockk.save(blogEntryDao = capture(blogEntryDaoSlot)) } returns blogEntry.toBlogEntryDao()

        blogServiceToTest.saveOrUpdate(blogEntry)
        val blogEntryDao = blogEntryDaoSlot.captured

        assertAll {
            assertThat(blogEntryDao.blogId).isEqualTo(owner.id)
            assertThat(blogEntryDao.creatorName).isEqualTo("me")
            assertThat(blogEntryDao.entry).isEqualTo("if i where a rich man...")
        }
    }
}
