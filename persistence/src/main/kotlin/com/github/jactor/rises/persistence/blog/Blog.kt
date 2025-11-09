package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.shared.api.BlogDto
import java.time.LocalDate
import java.util.UUID

@JvmRecord
data class Blog(
    internal val persistent: Persistent = Persistent(),

    val created: LocalDate?,
    val title: String,
    val userId: UUID?,
) {
    val id: UUID? get() = persistent.id

    fun toBlogDao() = BlogDao(
        id = persistent.id,
        created = created ?: persistent.timeOfCreation.toLocalDate(),
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        title = title,
        userId = userId,
    )

    fun toBlogDto() = BlogDto(
        persistentDto = persistent.toPersistentDto(),
        title = title,
        userId = userId
    )
}
