package com.github.jactor.persistence.dto

data class BlogEntryDto(
        var persistentDto: PersistentDto? = null,
        var blog: BlogDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, blogEntry: BlogEntryDto
    ) : this(
            persistent, blogEntry.blog, blogEntry.creatorName, blogEntry.entry
    )
}
