package com.github.jactor.persistence.guestbook

import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toGuestBookDao() = GuestBookDao(
    id = this[GuestBooks.id].value,
    createdBy = this[GuestBooks.createdBy],
    timeOfCreation = this[GuestBooks.timeOfCreation],
    modifiedBy = this[GuestBooks.modifiedBy],
    timeOfModification = this[GuestBooks.timeOfModification],

    title = this[GuestBooks.title],
    userId = this[GuestBooks.userId],
)

fun ResultRow.toGuestBookEntryDao() = GuestBookEntryDao(
    id = this[GuestBookEntries.id].value,
    createdBy = this[GuestBookEntries.createdBy],
    timeOfCreation = this[GuestBookEntries.timeOfCreation],
    modifiedBy = this[GuestBookEntries.modifiedBy],
    timeOfModification = this[GuestBookEntries.timeOfModification],

    guestName = this[GuestBookEntries.guestName],
    entry = this[GuestBookEntries.entry],
    guestBookId = this[GuestBookEntries.guestBookId],
)
