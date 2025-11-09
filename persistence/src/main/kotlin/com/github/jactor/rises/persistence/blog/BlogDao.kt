package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.PersistentDao
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class BlogDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var created: LocalDate = LocalDate.now(),
    var title: String = "",
    internal var userId: UUID? = null,
) : PersistentDao<BlogDao> {
    override fun copyWithoutId(): BlogDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toBlog(): Blog = Blog(
        created = created,
        persistent = toPersistent(),
        title = title,
        userId = userId
    )
}
