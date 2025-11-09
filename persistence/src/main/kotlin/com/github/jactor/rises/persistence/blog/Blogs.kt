package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.user.Users
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.datetime

object Blogs : UUIDTable(name = "T_BLOG", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val created = date("CREATED")
    val title = text("TITLE")
    val userId = uuid("USER_ID").references(Users.id)
}
