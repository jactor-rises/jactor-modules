package com.github.jactor.rises.persistence.person

import com.github.jactor.rises.persistence.UUIDv7
import com.github.jactor.rises.persistence.address.Addresses
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.datetime
import java.util.UUID

object People : IdTable<UUID>(name = "T_PERSON") {
    override val id: Column<EntityID<UUID>> = javaUUID("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val description = text("DESCRIPTION").nullable()
    val firstName = text("FIRST_NAME").nullable()
    val surname = text("SURNAME")
    val locale = text("LOCALE").nullable()
    val addressId = javaUUID("ADDRESS_ID").references(Addresses.id)
}
