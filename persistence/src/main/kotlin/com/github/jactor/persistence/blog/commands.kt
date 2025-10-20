package com.github.jactor.persistence.blog

import java.util.UUID

@JvmRecord
data class CreateBlogEntry(
    val blogId: UUID,
    val creatorName: String,
    val entry: String,
)

@JvmRecord
data class UpdateBlogTitle(
    val blogId: UUID,
    val title: String,
)
