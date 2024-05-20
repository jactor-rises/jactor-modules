package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.PersonInternalDto

internal object PersonBuilder {
    fun new(personInternalDto: PersonInternalDto = PersonInternalDto()): PersonData = PersonData(
        personInternalDto = personInternalDto.copy(
            persistentDto = personInternalDto.persistentDto.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(personInternalDto: PersonInternalDto): PersonData = PersonData(
        personInternalDto = personInternalDto
    )

    @JvmRecord
    data class PersonData(val personInternalDto: PersonInternalDto) {
        fun build(): PersonEntity = PersonEntity(person = personInternalDto)
    }
}
