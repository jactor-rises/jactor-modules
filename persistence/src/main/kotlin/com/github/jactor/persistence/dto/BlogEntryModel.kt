package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore

data class BlogEntryModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var blog: BlogModel? = null,
    var creatorName: String? = null,
    var entry: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("A creator is not provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("An entry is not provided!")

    constructor(
        persistentModel: PersistentModel, blogEntry: BlogEntryModel
    ) : this(
        persistentModel = persistentModel,
        blog = blogEntry.blog,
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry
    )
}
