package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.UUIDv7
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.datetime
import java.util.UUID

object BlogEntries : IdTable<UUID>(name = "T_BLOG_ENTRY") {
    override val id: Column<EntityID<UUID>> = javaUUID("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

    val blogId = javaUUID("BLOG_ID").references(Blogs.id)
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val creatorName = text("CREATOR_NAME")
    val entry = text("ENTRY")
}
