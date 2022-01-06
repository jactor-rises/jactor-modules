package com.github.jactor.persistence.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class BlogEntryDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var blog: BlogDto? = null,
    var creatorName: String? = null,
    var entry: String? = null
) : PersistentData(persistentDto) {
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("A creator is not provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("An entry is not provided!")

    constructor(
        persistentDto: PersistentDto, blogEntry: BlogEntryDto
    ) : this(
        persistentDto = persistentDto,
        blog = blogEntry.blog,
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry
    )
}
