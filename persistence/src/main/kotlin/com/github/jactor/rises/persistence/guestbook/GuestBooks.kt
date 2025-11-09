package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.user.Users
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object GuestBooks : UUIDTable(name = "T_GUEST_BOOK", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")
    val title = text("TITLE")
    val userId = uuid("USER_ID").references(Users.id)
}
