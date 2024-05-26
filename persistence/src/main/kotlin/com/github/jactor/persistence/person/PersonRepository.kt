package com.github.jactor.persistence.person

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface PersonRepository : CrudRepository<PersonEntity, UUID> {
    fun findBySurname(surname: String?): List<PersonEntity>
}
