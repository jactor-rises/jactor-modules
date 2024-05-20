package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.PersistentDto

internal object BlogBuilder {
    fun new(blogDto: BlogDto = BlogDto()): BlogData = BlogData(
        blogDto = blogDto.copy(persistentDto = blogDto.persistentDto.copy(id = UUID.randomUUID()))
    )

    fun unchanged(blogDto: BlogDto = BlogDto()): BlogData = BlogData(
        blogDto = blogDto
    )

    @JvmRecord
    data class BlogData(
        val blogDto: BlogDto,
        val blogEntryDto: BlogEntryDto? = null,
    ) {
        fun withEntry(blogEntryDto: BlogEntryDto): BlogData = copy(
            blogEntryDto = blogEntryDto.copy(
                persistentDto = PersistentDto(id = UUID.randomUUID()),
                blog = blogDto,
            )
        )

        fun withUnchangedEntry(blogEntryDto: BlogEntryDto): BlogData = copy(
            blogEntryDto = blogEntryDto,
        )

        fun buildBlogEntity(): BlogEntity = BlogEntity(blogDto = blogDto)

        fun buildBlogEntryEntity(): BlogEntryEntity = BlogEntryEntity(
            blogEntryDto = blogEntryDto ?: error("No blog entry dto"),
        )
    }
}
