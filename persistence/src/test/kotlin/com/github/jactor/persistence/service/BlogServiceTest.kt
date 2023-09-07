package com.github.jactor.persistence.service

import java.time.LocalDate
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntity.Companion.aBlog
import com.github.jactor.persistence.entity.BlogEntryEntity
import com.github.jactor.persistence.entity.BlogEntryEntity.Companion.aBlogEntry
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot

@ExtendWith(MockKExtension::class)
internal class BlogServiceTest {

    @InjectMockKs
    private lateinit var blogServiceToTest: BlogService

    @MockK
    private lateinit var blogRepositoryMock: BlogRepository

    @MockK
    private lateinit var blogEntryRepositoryMock: BlogEntryRepository

    @MockK
    private lateinit var userServiceMock: UserService

    @Test
    fun `should map blog to dto`() {
        val blogEntity = aBlog(BlogDto(PersistentDto(), null, "full speed ahead", null))

        every { blogRepositoryMock.findById(1001L) } returns Optional.of(blogEntity)

        val (_, _, title) = blogServiceToTest.find(1001L) ?: throw AssertionError("missed mocking?")

        assertThat(title).`as`("title").isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() {
        val blogEntryDto = BlogEntryDto(PersistentDto(), BlogDto(), "me", "too")
        val anEntry = aBlogEntry(blogEntryDto)

        every { blogEntryRepositoryMock.findById(1001L) } returns Optional.of(anEntry)

        val (_, _, creatorName, entry) = blogServiceToTest.findEntryBy(1001L)
            ?: throw AssertionError("missed mocking?")

        assertAll(
            { assertThat(creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(entry).`as`("entry").isEqualTo("too") }
        )
    }

    @Test
    fun `should find blogs for title`() {
        val blogsToFind = listOf(aBlog(BlogDto(title = "Star Wars")))

        every { blogRepositoryMock.findBlogsByTitle("Star Wars") } returns blogsToFind

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() {
        val blogEntryEntities: List<BlogEntryEntity?> = listOf(
            aBlogEntry(BlogEntryDto(PersistentDto(), BlogDto(), "you", "too"))
        )

        every { blogEntryRepositoryMock.findByBlog_Id(1001L) } returns blogEntryEntities

        val blogEntries = blogServiceToTest.findEntriesForBlog(1001L)

        assertAll(
            { assertThat(blogEntries).`as`("entries").hasSize(1) },
            { assertThat(blogEntries[0].creatorName).`as`("creator name").isEqualTo("you") },
            { assertThat(blogEntries[0].entry).`as`("entry").isEqualTo("too") }
        )
    }

    @Test
    fun `should save BlogDto as BlogEntity`() {
        val blogEntitySlot = slot<BlogEntity>()
        val blogDto = BlogDto()

        blogDto.created = LocalDate.now()
        blogDto.title = "some blog"
        blogDto.userInternal = UserInternalDto(username = "itsme")

        every { userServiceMock.find(username = any()) } returns null
        every { blogRepositoryMock.save(capture(blogEntitySlot)) } returns BlogEntity(blogDto)

        blogServiceToTest.saveOrUpdate(blogDto = blogDto)
        val blogEntity = blogEntitySlot.captured

        assertAll(
            { assertThat(blogEntity.created).`as`("created").isEqualTo(LocalDate.now()) },
            { assertThat(blogEntity.title).`as`("title").isEqualTo("some blog") },
            { assertThat(blogEntity.user).`as`("user").isNull() }
        )
    }

    @Test
    fun `should save BlogEntryDto as BlogEntryEntity`() {
        val blogEntryEntitySlot = slot<BlogEntryEntity>()
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.blog = BlogDto(userInternal = UserInternalDto(username = "itsme"))
        blogEntryDto.creatorName = "me"
        blogEntryDto.entry = "if i where a rich man..."

        every { userServiceMock.find(username = any()) } returns null
        every { blogEntryRepositoryMock.save(capture(blogEntryEntitySlot)) } returns BlogEntryEntity(blogEntryDto)

        blogServiceToTest.saveOrUpdate(blogEntryDto)
        val blogEntryEntity = blogEntryEntitySlot.captured

        assertAll(
            { assertThat(blogEntryEntity.blog).`as`("blog").isNotNull() },
            { assertThat(blogEntryEntity.creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(blogEntryEntity.entry).`as`("entry").isEqualTo("if i where a rich man...") }
        )
    }
}
