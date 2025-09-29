package com.github.jactor.persistence

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import com.github.jactor.persistence.test.withPersistedData
import com.github.jactor.persistence.test.withPersistentData
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest

internal class BlogServiceTest {

    @BeforeEach
    fun `mock repository objects`() = mockkObject(BlogRepositoryObject, UserRepositoryObject)

    @AfterEach
    fun `unmock repository objects`() = unmockkObject(BlogRepositoryObject, UserRepositoryObject)

    private val blogRepositoryMockk: BlogRepository = mockk {}
    private val blogServiceToTest: BlogService = BlogServiceImpl(blogRepository = blogRepositoryMockk)

    private val uuid: UUID = UUID.randomUUID()

    @Test
    fun `should map blog to dto`() = runTest {
        val blogDao = mockk<BlogDao> {
            every { toBlog() } returns Blog(
                created = null,
                persistent = Persistent(),
                title = "full speed ahead",
                user = null
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
                blog = initBlog(), creatorName = "me", entry = "too",
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
            }
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
                    blog = initBlog().withPersistentData(),
                    creatorName = "you",
                    entry = "too",
                    persistent = Persistent().withPersistedData(),
                )
            }
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
            user = owner
        )

        val blogDaoMockk = mockk<BlogDao>(relaxed = true) {
            every { toBlog() } returns blog
        }

        every { UserRepositoryObject.findById(id = blog.user?.id!!) } returns owner.toUserDao()
        every { blogRepositoryMockk.save(capture(blogDaoSlot)) } returns blogDaoMockk

        blogServiceToTest.saveOrUpdate(blog = blog)
        val blogDao = blogDaoSlot.captured

        assertAll {
            assertThat(blogDao.created).isEqualTo(LocalDate.now())
            assertThat(blogDao.title).isEqualTo("some blog")
            assertThat(blogDao.user).isEqualTo(owner.toUserDao())
        }
    }

    @Test
    fun `should save BlogEntryDto as BlogEntryEntity`() = runTest {
        val blogEntryDaoSlot = slot<BlogEntryDao>()
        val owner = initBlog(
            persistent = Persistent(id = UUID.randomUUID()),
            user = initUser(username = "itsme").withId(),
        )

        val blogEntry = BlogEntry(
            blog = owner,
            creatorName = "me",
            entry = "if i where a rich man..."
        )

        every { BlogRepositoryObject.findBlogById(id = blogEntry.blog?.id!!) } returns owner.toBlogDao()
        every { UserRepositoryObject.findById(id = blogEntry.blog?.user?.id!!) } returns owner.user?.toUserDao()
        every { blogRepositoryMockk.save(blogEntryDao = capture(blogEntryDaoSlot)) } returns blogEntry.toBlogEntryDao()

        blogServiceToTest.saveOrUpdate(blogEntry)
        val blogEntryEntity = blogEntryDaoSlot.captured

        assertAll {
            assertThat(blogEntryEntity.blogDao).isNotNull()
            assertThat(blogEntryEntity.creatorName).isEqualTo("me")
            assertThat(blogEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
