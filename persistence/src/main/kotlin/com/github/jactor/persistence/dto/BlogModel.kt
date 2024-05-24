package com.github.jactor.persistence.dto

import java.time.LocalDate

data class BlogModel(
    override val persistentDto: PersistentDto = PersistentDto(),
    var created: LocalDate? = null,
    var title: String? = null,
    var userInternal: UserModel? = null
) : PersistentDataModel(persistentDto) {
    constructor(
        persistentDto: PersistentDto, blog: BlogModel
    ) : this(
        persistentDto = persistentDto,
        created = blog.created,
        title = blog.title,
        userInternal = blog.userInternal
    )
}