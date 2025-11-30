package com.github.jactor.rises.persistence.address

import org.springframework.stereotype.Repository
import java.util.UUID

interface AddressRepository {
    fun findById(id: UUID): AddressDao?
}

@Repository
class AddressRepositoryImpl : AddressRepository by AddressRepositoryObject
