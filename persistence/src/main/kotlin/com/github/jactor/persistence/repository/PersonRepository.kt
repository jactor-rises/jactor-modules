package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.PersonEntity
import org.springframework.data.repository.CrudRepository

interface PersonRepository : CrudRepository<PersonEntity, Long> {
    fun findBySurname(surname: String?): List<PersonEntity>
}
