package com.github.jactor.persistence.service

import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class PersonService(private val personRepository: PersonRepository) {
    fun createWhenNotExists(person: PersonInternalDto): PersonEntity? {
        return findExisting(person)
            .orElseGet { create(person) }
    }

    private fun create(person: PersonInternalDto): PersonEntity {
        return personRepository.save(PersonEntity(person))
    }

    private fun findExisting(person: PersonInternalDto): Optional<PersonEntity?> {
        return Optional.ofNullable(person.id)
            .flatMap { id: Long -> personRepository.findById(id) }
    }
}
