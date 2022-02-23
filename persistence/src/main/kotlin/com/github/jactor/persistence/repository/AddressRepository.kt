package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.AddressEntity
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<AddressEntity, Long> {
    fun findByZipCode(zipCode: String): List<AddressEntity>
}
