package com.gitlab.jactor.persistence.dto

data class BlogEntryDto(
        var persistentDto: PersistentDto? = null,
        var blog: BlogDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, blogEntry: BlogEntryDto
    ) : this(
            persistent, blogEntry.blog, blogEntry.creatorName, blogEntry.entry
    )

    override fun fetchPersistentDto(): PersistentDto {
        if (persistentDto != null) {
            return persistentDto as PersistentDto
        }

        return PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long) {
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        persistentDto!!.id = id
    }
}
