package com.github.jactor.persistence.address

import java.util.UUID
import org.springframework.stereotype.Repository

interface AddressRepository {
    fun findById(id: UUID): AddressDao?
    fun findByZipCode(zipCode: String): List<AddressDao>
    fun save(addressDao: AddressDao): AddressDao
}

@Repository
class AddressRepositoryImpl : AddressRepository by AddressRepositoryObject
