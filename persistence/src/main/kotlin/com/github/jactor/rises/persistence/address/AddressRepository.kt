package com.github.jactor.rises.persistence.address

import java.util.UUID
import org.springframework.stereotype.Repository

interface AddressRepository {
    fun findById(id: UUID): AddressDao?
}

@Repository
class AddressRepositoryImpl : AddressRepository by AddressRepositoryObject
