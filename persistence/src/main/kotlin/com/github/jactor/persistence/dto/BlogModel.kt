package com.github.jactor.persistence.dto

import java.time.LocalDate
import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore

data class BlogModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var created: LocalDate? = null,
    var title: String? = null,
    var userInternal: UserModel? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, blog: BlogModel
    ) : this(
        persistentModel = persistentModel,
        created = blog.created,
        title = blog.title,
        userInternal = blog.userInternal
    )
}