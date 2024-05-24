package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.BlogModel
import com.github.jactor.persistence.dto.BlogEntryModel
import com.github.jactor.persistence.dto.PersistentDto

internal object BlogBuilder {
    fun new(blogModel: BlogModel = BlogModel()): BlogData = BlogData(
        blogModel = blogModel.copy(persistentDto = blogModel.persistentDto.copy(id = UUID.randomUUID()))
    )

    fun unchanged(blogModel: BlogModel = BlogModel()): BlogData = BlogData(
        blogModel = blogModel
    )

    @JvmRecord
    data class BlogData(
        val blogModel: BlogModel,
        val blogEntryModel: BlogEntryModel? = null,
    ) {
        fun withEntry(blogEntryModel: BlogEntryModel): BlogData = copy(
            blogEntryModel = blogEntryModel.copy(
                persistentDto = PersistentDto(id = UUID.randomUUID()),
                blog = blogModel,
            )
        )

        fun withUnchangedEntry(blogEntryModel: BlogEntryModel): BlogData = copy(
            blogEntryModel = blogEntryModel,
        )

        fun buildBlogEntity(): BlogEntity = BlogEntity(blogModel = blogModel)

        fun buildBlogEntryEntity(): BlogEntryEntity = BlogEntryEntity(
            blogEntryModel = blogEntryModel ?: error("No blog entry dto"),
        )
    }
}
