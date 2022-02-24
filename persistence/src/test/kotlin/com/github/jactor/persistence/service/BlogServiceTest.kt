package com.github.jactor.persistence.service

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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.Optional
import java.util.function.Supplier

@ExtendWith(MockitoExtension::class)
internal class BlogServiceTest {

    @InjectMocks
    private lateinit var blogServiceToTest: BlogService

    @Mock
    private lateinit var blogRepositoryMock: BlogRepository

    @Mock
    private lateinit var blogEntryRepositoryMock: BlogEntryRepository

    @Mock
    private lateinit var userServiceMock: UserService

    @Test
    fun `should map blog to dto`() {
        val blogEntity = Optional.of(aBlog(BlogDto(PersistentDto(), null, "full speed ahead", null)))

        whenever(blogRepositoryMock.findById(1001L)).thenReturn(blogEntity)

        val (_, _, title) = blogServiceToTest.find(1001L).orElseThrow(mockError())

        assertThat(title).`as`("title").isEqualTo("full speed ahead")
    }

    @Test
    fun `should map blog entry to dto`() {
        val blogEntryDto = BlogEntryDto(PersistentDto(), BlogDto(), "me", "too")
        val anEntry = Optional.of(aBlogEntry(blogEntryDto))

        whenever(blogEntryRepositoryMock.findById(1001L)).thenReturn(anEntry)

        val (_, _, creatorName, entry) = blogServiceToTest.findEntryBy(1001L).orElseThrow(mockError())

        assertAll(
            { assertThat(creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(entry).`as`("entry").isEqualTo("too") }
        )
    }

    private fun mockError(): Supplier<AssertionError> {
        return Supplier { AssertionError("missed mocking?") }
    }

    @Test
    fun `should find blogs for title`() {
        val blogsToFind = listOf(aBlog(BlogDto(title = "Star Wars")))

        whenever(blogRepositoryMock.findBlogsByTitle("Star Wars")).thenReturn(blogsToFind)

        val blogForTitle = blogServiceToTest.findBlogsBy("Star Wars")

        assertThat(blogForTitle).hasSize(1)
    }

    @Test
    fun `should map blog entries to a list of dto`() {
        val blogEntryEntities: List<BlogEntryEntity?> = listOf(
            aBlogEntry(BlogEntryDto(PersistentDto(), BlogDto(), "you", "too"))
        )

        whenever(blogEntryRepositoryMock.findByBlog_Id(1001L)).thenReturn(blogEntryEntities)

        val blogEntries = blogServiceToTest.findEntriesForBlog(1001L)

        assertAll(
            { assertThat(blogEntries).`as`("entries").hasSize(1) },
            { assertThat(blogEntries[0].creatorName).`as`("creator name").isEqualTo("you") },
            { assertThat(blogEntries[0].entry).`as`("entry").isEqualTo("too") }
        )
    }

    @Test
    fun `should save BlogDto as BlogEntity`() {
        val blogDto = BlogDto()

        blogDto.created = LocalDate.now()
        blogDto.title = "some blog"
        blogDto.userInternal = UserInternalDto(username = "itsme")

        whenever(blogRepositoryMock.save(BlogEntity(blogDto))).thenReturn(BlogEntity(blogDto))

        blogServiceToTest.saveOrUpdate(blogDto)
        val argCaptor = ArgumentCaptor.forClass(BlogEntity::class.java)
        verify(blogRepositoryMock).save(argCaptor.capture())
        val blogEntity = argCaptor.value

        assertAll(
            { assertThat(blogEntity.created).`as`("created").isEqualTo(LocalDate.now()) },
            { assertThat(blogEntity.title).`as`("title").isEqualTo("some blog") },
            { assertThat(blogEntity.user).`as`("user").isNotNull() }
        )
    }

    @Test
    fun `should save BlogEntryDto as BlogEntryEntity`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.blog = BlogDto(userInternal = UserInternalDto(username = "itsme"))
        blogEntryDto.creatorName = "me"
        blogEntryDto.entry = "if i where a rich man..."

        whenever(blogEntryRepositoryMock.save(BlogEntryEntity(blogEntryDto))).thenReturn(BlogEntryEntity(blogEntryDto))

        blogServiceToTest.saveOrUpdate(blogEntryDto)
        val argCaptor = ArgumentCaptor.forClass(BlogEntryEntity::class.java)
        verify(blogEntryRepositoryMock).save(argCaptor.capture())
        val blogEntryEntity = argCaptor.value

        assertAll(
            { assertThat(blogEntryEntity.blog).`as`("blog").isNotNull() },
            { assertThat(blogEntryEntity.creatorName).`as`("creator name").isEqualTo("me") },
            { assertThat(blogEntryEntity.entry).`as`("entry").isEqualTo("if i where a rich man...") }
        )
    }
}
