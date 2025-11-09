package com.github.jactor.rises.persistence.user

import com.github.jactor.rises.persistence.person.People
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object Users : UUIDTable(name = "T_USER", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val emailAddress = text("EMAIL").nullable()
    val username = text("USER_NAME")
    val personId = uuid("PERSON_ID").references(People.id)
    val userType = text("USER_TYPE")
    val inactiveSince = datetime("INACTIVE_SINCE").nullable()
}
