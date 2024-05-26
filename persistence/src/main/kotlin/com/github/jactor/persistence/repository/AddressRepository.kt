package com.github.jactor.persistence.repository

import java.util.UUID
import com.github.jactor.persistence.address.AddressEntity
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<AddressEntity, UUID> {
    fun findByZipCode(zipCode: String): List<AddressEntity>
}
