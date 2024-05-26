package com.github.jactor.persistence.blog

import java.time.LocalDate
import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.shared.api.BlogDto

@JvmRecord
data class BlogModel(
    val created: LocalDate? = null,
    val persistentModel: PersistentModel = PersistentModel(),
    val title: String? = null,
    val user: UserModel? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(blogDto: BlogDto) : this(
        persistentModel = PersistentModel(blogDto.persistentDto),
        title = blogDto.title,
        user = blogDto.user?.let { UserModel(userDto = it) }
    )

    constructor(persistentModel: PersistentModel, blog: BlogModel) : this(
        persistentModel = persistentModel,
        created = blog.created,
        title = blog.title,
        user = blog.user
    )

    fun toDto() = BlogDto(
        persistentDto = persistentModel.toDto(),
        title = title,
        user = user?.toDto()
    )
}
