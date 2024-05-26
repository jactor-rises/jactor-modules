package com.github.jactor.persistence.address

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<AddressEntity, UUID> {
    fun findByZipCode(zipCode: String): List<AddressEntity>
}
