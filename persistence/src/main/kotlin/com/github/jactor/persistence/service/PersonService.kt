package com.github.jactor.persistence.service

import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository

@Service
class PersonService(private val personRepository: PersonRepository) {
    fun createWhenNotExists(person: PersonModel): PersonEntity? {
        return findExisting(person) ?: create(person)
    }

    private fun create(person: PersonModel): PersonEntity {
        return personRepository.save(
            PersonEntity(
                person = person.copy(
                    persistentModel = person.persistentModel.copy(
                        id = person.id ?: UUID.randomUUID()
                    )
                )
            )
        )
    }

    private fun findExisting(person: PersonModel): PersonEntity? {
        return person.id?.let { personRepository.findById(it).getOrNull() }
    }
}
