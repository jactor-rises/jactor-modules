package com.github.jactor.persistence.service

import kotlin.jvm.optionals.getOrNull
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(private val personRepository: PersonRepository) {
    fun createWhenNotExists(person: PersonInternalDto): PersonEntity? {
        return findExisting(person) ?: create(person)
    }

    private fun create(person: PersonInternalDto): PersonEntity {
        return personRepository.save(PersonEntity(person))
    }

    private fun findExisting(person: PersonInternalDto): PersonEntity? {
        return person.id?.let { personRepository.findById(it).getOrNull() }
    }
}
