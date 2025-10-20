package com.github.jactor.persistence.address

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.javatime.datetime

object Addresses : UUIDTable(name = "T_ADDRESS", columnName = "ID") {
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
