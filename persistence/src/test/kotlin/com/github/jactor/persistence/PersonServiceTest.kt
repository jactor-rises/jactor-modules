package com.github.jactor.persistence

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.PersistentModel
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class PersonServiceTest @Autowired constructor(
    private val personRepository: PersonRepository,
    private val personService: PersonService
) : AbstractSpringBootNoDirtyContextTest() {

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
                    createdBy = "creator",
                    id = personEntity.id,
                    modifiedBy = "modifier",
                    timeOfCreation = LocalDateTime.now(),
                    timeOfModification = LocalDateTime.now()
                ),
                PersonModel()
            )
        )

        // then
        assertThat(person).isEqualTo(personEntity)
    }
}
