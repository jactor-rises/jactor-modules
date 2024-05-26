package com.github.jactor.persistence.blog

import java.util.UUID
import com.github.jactor.persistence.dto.PersistentModel

internal object BlogBuilder {
    fun new(blogModel: BlogModel = BlogModel()): BlogData = BlogData(
        blogModel = blogModel.copy(persistentModel = blogModel.persistentModel.copy(id = UUID.randomUUID()))
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
                persistentModel = PersistentModel(id = UUID.randomUUID()),
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
