package com.github.jactor.persistence.person

import java.util.UUID
import org.springframework.stereotype.Repository

interface PersonRepository {
    fun findAll(): List<PersonDao>
    fun findById(id: UUID): PersonDao?
    fun findBySurname(surname: String?): List<PersonDao>
    fun save(personDao: PersonDao): PersonDao
}

@Repository
class PersonRepositoryImpl : PersonRepository by PersonRepositoryObject
