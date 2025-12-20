package com.github.jactor.rises.persistence.person

import com.github.jactor.rises.persistence.PersistenceHandler
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val persistenceHandler: PersistenceHandler,
) {
    suspend fun createWhenNotExists(person: Person): PersonDao? = findExisting(person) ?: create(person)
    private suspend fun findExisting(person: Person): PersonDao? = person.id?.let { personRepository.findById(it) }
    private suspend fun create(person: Person): PersonDao = persistenceHandler.modifyAndSave(
        dao = person.toPersonDao(),
    ) { personRepository.save(personDao = it) }
}
