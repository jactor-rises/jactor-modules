package com.github.jactor.persistence.service

import java.time.LocalDateTime
import java.util.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import io.mockk.called
import io.mockk.every
import io.mockk.slot
import io.mockk.verify

internal class PersonServiceTest : AbstractSpringBootNoDirtyContextTest() {

    @Autowired
    private lateinit var personService: PersonService

    @Test
    fun `should create a new Person`() {
        val entity = personService.createWhenNotExists(PersonInternalDto())

        assertThat(entity).isNotNull
    }

    @Test
    fun `should find Person by id`() {
        val personEntity = personRepository.save(PersonEntity())

        // when
        val person = personService.createWhenNotExists(
            PersonInternalDto(
                PersistentDto(
                    id = personEntity.id,
                    createdBy = "creator",
                    timeOfCreation = LocalDateTime.now(),
                    modifiedBy = "modifier", LocalDateTime.now()
                ),
                PersonInternalDto()
            )
        )

        // then
        assertThat(person).`as`("person").isEqualTo(personEntity)
    }
}
