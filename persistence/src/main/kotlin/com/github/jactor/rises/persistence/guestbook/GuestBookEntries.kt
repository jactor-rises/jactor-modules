package com.github.jactor.rises.persistence.guestbook

import java.util.UUID
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.javatime.datetime
import com.github.jactor.rises.persistence.UUIDv7

object GuestBookEntries : IdTable<UUID>(name = "T_GUEST_BOOK_ENTRY") {
    override val id: Column<EntityID<UUID>> = uuid("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

    val createdBy = text("CREATED_BY")
    val guestName = text("GUEST_NAME")
    val entry = text("ENTRY")
    val guestBookId = uuid("GUEST_BOOK_ID").references(GuestBooks.id)
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")
}
