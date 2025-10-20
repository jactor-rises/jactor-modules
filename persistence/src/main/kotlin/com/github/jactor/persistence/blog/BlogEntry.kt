package com.github.jactor.persistence.blog

import com.github.jactor.persistence.Persistent
import com.github.jactor.shared.api.BlogEntryDto
import java.util.UUID

@JvmRecord
data class BlogEntry(
    val blogId: UUID,
    val creatorName: String,
    val entry: String,
    val persistent: Persistent = Persistent(),
) {
    val id: UUID? get() = persistent.id

    fun toBlogEntryDto() = BlogEntryDto(
        persistentDto = persistent.toPersistentDto(),
        blogId = blogId,
        creatorName = creatorName,
        entry = entry,
    )

    fun toBlogEntryDao() = BlogEntryDao(
        id = persistent.id,

        blogId = blogId,
        createdBy = persistent.createdBy,
        creatorName = creatorName,
        entry = entry,
        timeOfCreation = persistent.timeOfCreation,
        modifiedBy = persistent.modifiedBy,
        timeOfModification = persistent.timeOfModification,
    )
}
