package com.github.jactor.rises.persistence.address

import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import com.github.jactor.rises.persistence.guestbook.toAddressDao

object AddressRepositoryObject : AddressRepository {
    override fun findById(id: UUID): AddressDao? = Addresses
        .selectAll()
        .andWhere { Addresses.id eq id }
        .singleOrNull()
        ?.toAddressDao()
}
