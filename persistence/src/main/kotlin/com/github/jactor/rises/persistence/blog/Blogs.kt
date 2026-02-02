package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.UUIDv7
import com.github.jactor.rises.persistence.user.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.datetime
import java.util.UUID

object Blogs : IdTable<UUID>(name = "T_BLOG") {
    override val id: Column<EntityID<UUID>> = javaUUID("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val created = date("CREATED")
    val title = text("TITLE")
    val userId = javaUUID("USER_ID").references(Users.id)
}
