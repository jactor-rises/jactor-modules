package com.github.jactor.rises.persistence.person

import assertk.assertThat
import assertk.assertions.isNotNull
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.rises.persistence.test.initPerson
import com.github.jactor.rises.shared.test.all
import com.github.jactor.rises.shared.test.equals
import com.github.jactor.rises.shared.test.named
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

internal class PersonServiceTest @Autowired constructor(
    private val personService: PersonService,
    private val personRepository: PersonRepository,
) : AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should create a new Person`() = runTest {
        val dao = personService.createWhenNotExists(initPerson())

        assertThat(dao).isNotNull()
    }

    @Test
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
                    timeOfModification = LocalDateTime.now(),
                ),
            ),
        )

        // then
        assertThat(person).all {
            id named "id" equals personDao.id
            surname named "surname" equals personDao.surname
            firstName named "firstName" equals personDao.firstName
            description named "description" equals personDao.description
        }
    }
}
