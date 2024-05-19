package com.github.jactor.persistence.service

import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository

@Service
class PersonService(private val personRepository: PersonRepository) {
    fun createWhenNotExists(person: PersonInternalDto): PersonEntity? {
        return findExisting(person) ?: create(person)
    }

    private fun create(person: PersonInternalDto): PersonEntity {
        if (person.id == null) {
            person.id = UUID.randomUUID()
        }

        return personRepository.save(PersonEntity(person))
    }

    private fun findExisting(person: PersonInternalDto): PersonEntity? {
        return person.id?.let { personRepository.findById(it).getOrNull() }
    }
}
