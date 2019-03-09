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
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        return persistentDto as PersistentDto
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}