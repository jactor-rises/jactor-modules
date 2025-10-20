package com.github.jactor.persistence.person

import org.springframework.stereotype.Service

@Service
class PersonService(private val personRepository: PersonRepository) {
    suspend fun createWhenNotExists(person: Person): PersonDao? = findExisting(person) ?: create(person)
    private suspend fun create(person: Person): PersonDao = personRepository.save(personDao = person.toPersonDao())
    private suspend fun findExisting(person: Person): PersonDao? = person.id?.let {
        personRepository.findById(it)
    }
}
