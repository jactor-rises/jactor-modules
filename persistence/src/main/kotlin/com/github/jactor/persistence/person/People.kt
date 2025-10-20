package com.github.jactor.persistence.person

import com.github.jactor.persistence.address.Addresses
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object People : UUIDTable(name = "T_PERSON", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val description = text("DESCRIPTION").nullable()
    val firstName = text("FIRST_NAME").nullable()
    val surname = text("SURNAME")
    val locale = text("LOCALE").nullable()
    val addressId = uuid("ADDRESS_ID").references(Addresses.id)
}