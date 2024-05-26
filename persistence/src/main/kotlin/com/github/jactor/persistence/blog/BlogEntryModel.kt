package com.github.jactor.persistence.blog

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.shared.api.BlogEntryDto

@JvmRecord
data class BlogEntryModel(
    val blog: BlogModel? = null,
    val creatorName: String? = null,
    val entry: String? = null,
    val persistentModel: PersistentModel = PersistentModel(),
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id
    val notNullableEntry: String @JsonIgnore get() = entry ?: throw IllegalStateException("An entry is not provided!")
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("A creator is not provided!")

    constructor(blogEntry: BlogEntryDto) : this(
        persistentModel = PersistentModel(blogEntry.persistentDto),
        blog = blogEntry.blogDto?.let { BlogModel(blogDto = it) },
        entry = blogEntry.entry
    )

    constructor(persistentModel: PersistentModel, blogEntry: BlogEntryModel) : this(
        persistentModel = persistentModel,
        blog = blogEntry.blog,
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry
    )

    fun toDto() = BlogEntryDto(
        persistentDto = persistentModel.toDto(),
        blogDto = blog?.toDto(),
        creatorName = creatorName,
        entry = entry,
    )
}
