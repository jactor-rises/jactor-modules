package com.github.jactor.rises.persistence.address

import com.github.jactor.rises.persistence.UUIDv7
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.datetime
import java.util.UUID

object Addresses : IdTable<UUID>(name = "T_ADDRESS") {
    override val id: Column<EntityID<UUID>> = javaUUID("ID")
        .clientDefault { UUIDv7.generate() }
        .entityId()

    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val addressLine1 = text("ADDRESS_LINE_1")
    val addressLine2 = text("ADDRESS_LINE_2").nullable()
    val addressLine3 = text("ADDRESS_LINE_3").nullable()
    val city = text("CITY")
    val country = text("COUNTRY").nullable()
    val zipCode = text("ZIP_CODE")
}
