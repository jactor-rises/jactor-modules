package com.github.jactor.persistence.dto

import java.time.LocalDate

data class BlogDto(
        var persistentDto: PersistentDto? = null,
        var created: LocalDate? = null,
        var title: String? = null,
        var user: UserDto? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, blog: BlogDto
    ) : this(
            persistent, blog.created, blog.title, blog.user
    )

    override fun fetchPersistentDto(): PersistentDto {
        return if (persistentDto != null) persistentDto as PersistentDto else PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}