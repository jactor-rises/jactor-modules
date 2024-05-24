package com.github.jactor.persistence.service

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.entity.PersonBuilder
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class PersonServiceTest : AbstractSpringBootNoDirtyContextTest() {

    @Autowired
    private lateinit var personService: PersonService

    @Test
    fun `should create a new Person`() {
        val entity = personService.createWhenNotExists(PersonModel())

        assertThat(entity).isNotNull()
    }

    @Test
    fun `should find Person by id`() {
        val personEntity = personRepository.save(PersonBuilder.new().build())

        // when
        val person = personService.createWhenNotExists(
            PersonModel(
                PersistentModel(
                    id = personEntity.id,
                    createdBy = "creator",
                    timeOfCreation = LocalDateTime.now(),
                    modifiedBy = "modifier", LocalDateTime.now()
                ),
                PersonModel()
            )
        )

        // then
        assertThat(person).isEqualTo(personEntity)
    }
}
