package com.github.jactor.rises.persistence.user

import java.util.UUID
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.javatime.datetime
import com.github.jactor.rises.persistence.UUIDv7
import com.github.jactor.rises.persistence.person.People

object Users : IdTable<UUID>(name = "T_USER") {
    override val id: Column<EntityID<UUID>> = uuid("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

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
