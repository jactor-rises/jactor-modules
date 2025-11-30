package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.EntryDao
import com.github.jactor.rises.persistence.PersistentDao
import java.time.LocalDateTime
import java.util.UUID

data class BlogEntryDao(
    override var id: UUID? = null,
    override val createdBy: String = "todo",
    override var creatorName: String,
    override var entry: String,
    override var modifiedBy: String = "todo",
    override val timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),
    internal var blogId: UUID,
) : PersistentDao<BlogEntryDao>, EntryDao {
    override fun copyWithoutId(): BlogEntryDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogEntryDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toBlogEntry() = BlogEntry(
        persistent = toPersistent(),
        blogId = blogId,
        creatorName = creatorName,
        entry = entry,
    )
}
