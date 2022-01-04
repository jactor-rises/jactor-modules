package com.github.jactor.persistence.dto

data class BlogEntryDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var blog: BlogDto? = null,
    var creatorName: String? = null,
    var entry: String? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, blogEntry: BlogEntryDto
    ) : this(
        persistentDto = persistentDto,
        blog = blogEntry.blog,
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry
    )
}
