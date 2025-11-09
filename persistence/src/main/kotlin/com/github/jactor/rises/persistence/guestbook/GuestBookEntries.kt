package com.github.jactor.rises.persistence.guestbook

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object GuestBookEntries : UUIDTable(name = "T_GUEST_BOOK_ENTRY", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val guestName = text("GUEST_NAME")
    val entry = text("ENTRY")
    val guestBookId = uuid("GUEST_BOOK_ID").references(GuestBooks.id)
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")
}
