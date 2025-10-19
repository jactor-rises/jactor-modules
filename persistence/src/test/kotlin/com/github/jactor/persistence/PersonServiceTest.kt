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
import org.junit.jupiter.api.Disabled

internal class PersonServiceTest @Autowired constructor(
    private val personService: PersonService,
    private val personRepository: PersonRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should create a new Person`() = runTest {
        val entity = personService.createWhenNotExists(initPerson())

        assertThat(entity).isNotNull()
    }

    @Test
    @Disabled("Disabled because of tansaction management on CI???, but not locally")
    fun `should find Person by id`() = runTest {
        val personDao = personRepository.save(initPerson().toPersonDao())

        // when
        val person = personService.createWhenNotExists(
            person = initPerson(
                persistent = Persistent(
                    createdBy = "creator",
                    id = personDao.id,
                    modifiedBy = "modifier",
                    timeOfCreation = LocalDateTime.now(),
                    timeOfModification = LocalDateTime.now()
                ),
            )
        )

        // then
        assertThat(person).isEqualTo(personDao)
    }
}
