package com.github.jactor.persistence.blog

import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.user.UserService
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

    private val blogServiceToTest: BlogService = DefaultBlogService(
        blogRepository = blogRepositoryMockk,
        blogEntryRepository = blogEntryRepositoryMockk,
        userService = userServiceMockk
    )

    private val uuid: UUID = UUID.randomUUID()

    @Test
    fun `should map blog to dto`() {
        val blogEntity = BlogBuilder.new(
            blogModel = BlogModel(
                created = null,
                persistentModel = PersistentModel(),
                title = "full speed ahead",
                user = null
            )
        ).buildBlogEntity()

        every { blogRepositoryMockk.findById(uuid) } returns Optional.of(blogEntity)

        val (_, _, title) = blogServiceToTest.find(uuid) ?: throw AssertionError("missed mocking?")

        assertThat(title).isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() {
        val blogEntryModel = BlogEntryModel(
            blog = BlogModel(), creatorName = "me", entry = "too",
            persistentModel = PersistentModel(),
        )
        val anEntry = BlogBuilder.new().withEntry(blogEntryModel = blogEntryModel).buildBlogEntryEntity()

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
        val blogsToFind = listOf(BlogBuilder.new(blogModel = BlogModel(title = "Star Wars")).buildBlogEntity())

        every { blogRepositoryMockk.findBlogsByTitle("Star Wars") } returns blogsToFind

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() {
        val blogEntryEntities: List<BlogEntryEntity?> = listOf(
            BlogBuilder.new().withEntry(
                blogEntryModel = BlogEntryModel(
                    blog = BlogModel(),
                    creatorName = "you",
                    entry = "too",
                    persistentModel = PersistentModel(),
                )
            ).buildBlogEntryEntity()
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
        val blogModel = BlogModel(
            created = LocalDate.now(),
            title = "some blog",
            user = UserModel(username = "itsme")
        )

        every { userServiceMockk.find(username = any()) } returns null
        every { blogRepositoryMockk.save(capture(blogEntitySlot)) } returns BlogEntity(blogModel)

        blogServiceToTest.saveOrUpdate(blogModel = blogModel)
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
        val blogEntryModel = BlogEntryModel(
            blog = BlogModel(
                persistentModel = PersistentModel(id = UUID.randomUUID()),
                user = UserModel(username = "itsme"),
            ),
            creatorName = "me",
            entry = "if i where a rich man..."
        )



        every { userServiceMockk.find(username = any()) } returns null
        every { blogEntryRepositoryMockk.save(capture(blogEntryEntitySlot)) } returns BlogEntryEntity(blogEntryModel)

        blogServiceToTest.saveOrUpdate(blogEntryModel)
        val blogEntryEntity = blogEntryEntitySlot.captured

        assertAll {
            assertThat(blogEntryEntity.blog).isNotNull()
            assertThat(blogEntryEntity.creatorName).isEqualTo("me")
            assertThat(blogEntryEntity.entry).isEqualTo("if i where a rich man...")
        }
    }
}
