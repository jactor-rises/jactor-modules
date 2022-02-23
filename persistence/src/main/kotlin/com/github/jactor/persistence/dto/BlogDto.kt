package com.github.jactor.persistence.dto

import java.time.LocalDate

data class BlogDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var created: LocalDate? = null,
    var title: String? = null,
    var userInternal: UserInternalDto? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, blog: BlogDto
    ) : this(
        persistentDto = persistentDto,
        created = blog.created,
        title = blog.title,
        userInternal = blog.userInternal
    )
}