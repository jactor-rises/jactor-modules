package com.gitlab.jactor.persistence.dto

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