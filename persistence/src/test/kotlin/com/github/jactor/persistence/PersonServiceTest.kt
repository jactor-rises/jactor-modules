package com.github.jactor.persistence

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initPerson
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinx.coroutines.test.runTest

internal class PersonServiceTest @Autowired constructor(
    private val personRepository: PersonRepository,
    private val personService: PersonService
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should create a new Person`() = runTest {
        val entity = personService.createWhenNotExists(initPerson())

        assertThat(entity).isNotNull()
    }

    @Test
    fun `should find Person by id`() = runTest {
        val personEntity = personRepository.insertOrUpdate(initPerson().toEntityWithId())

        // when
        val person = personService.createWhenNotExists(
            Person(
                persistent = Persistent(
                    createdBy = "creator",
                    id = personEntity.id,
                    modifiedBy = "modifier",
                    timeOfCreation = LocalDateTime.now(),
                    timeOfModification = LocalDateTime.now()
                ),
                person = initPerson()
            )
        )

        // then
        assertThat(person).isEqualTo(personEntity)
    }
}
