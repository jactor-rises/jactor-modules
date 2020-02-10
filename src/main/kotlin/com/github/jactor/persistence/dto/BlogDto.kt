package com.github.jactor.persistence.dto

import java.time.LocalDate

data class BlogDto(
        var persistentDto: PersistentDto? = null,
        var created: LocalDate? = null,
        var title: String? = null,
        var userInternal: UserInternalDto? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, blog: BlogDto
    ) : this(
            persistent, blog.created, blog.title, blog.userInternal
    )
}