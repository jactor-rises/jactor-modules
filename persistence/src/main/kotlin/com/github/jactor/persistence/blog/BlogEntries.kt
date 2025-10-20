package com.github.jactor.persistence.blog

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object BlogEntries : UUIDTable(name = "T_BLOG_ENTRY", columnName = "ID") {
    val blogId = uuid("BLOG_ID").references(Blogs.id)
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val creatorName = text("CREATOR_NAME")
    val entry = text("ENTRY")
}
