package com.github.jactor.rises.persistence.address

import com.github.jactor.rises.persistence.util.toAddressDao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID

object AddressRepositoryObject : AddressRepository {
    override fun findById(id: UUID): AddressDao? = Addresses
        .selectAll()
        .andWhere { Addresses.id eq id }
        .singleOrNull()
        ?.toAddressDao()
}
