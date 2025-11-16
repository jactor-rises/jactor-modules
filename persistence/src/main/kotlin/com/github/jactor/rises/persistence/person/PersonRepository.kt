package com.github.jactor.rises.persistence.person

import java.util.UUID
import org.springframework.stereotype.Repository

interface PersonRepository {
    fun findById(id: UUID): PersonDao?
    fun save(personDao: PersonDao): PersonDao
}

@Repository
class PersonRepositoryImpl : PersonRepository by PersonRepositoryObject
