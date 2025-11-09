package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.PersistentDao
import java.time.LocalDateTime
import java.util.UUID

data class GuestBookDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var title: String = "no-name",
    internal var userId: UUID? = null,
) : PersistentDao<GuestBookDao?> {
    fun toGuestBook(): GuestBook = GuestBook(
        persistent = toPersistent(),
        title = title,
        userId = userId
    )

    override fun copyWithoutId(): GuestBookDao = copy(
        id = null,
    )

    override fun modifiedBy(modifier: String): GuestBookDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }
}
