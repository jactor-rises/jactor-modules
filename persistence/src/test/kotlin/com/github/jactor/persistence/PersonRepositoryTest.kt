package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class PersonRepositoryTest @Autowired constructor(
    private val personRepository: PersonRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should find default persons`() {
        val firstNames = personRepository.findBySurname("Jacobsen")
            .map { it.firstName }

        assertAll {
            assertThat(firstNames).contains("Tor Egil")
            assertThat(firstNames).contains("Suthatip")
        }
    }

    @Test
    fun `should save then read a person entity`() {
        val allreadyPresentPeople = personRepository.findAll().count()
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington"
        )

        val personToPersist = initPerson(
            address = address,
            firstName = "Born",
            description = "Me, myself, and I",
            locale = "no_NO",
            surname = "Sometime",
        ).toPersonDao()

        personRepository.save(personToPersist)

        val people = personRepository.findAll()
        assertThat(people).hasSize(allreadyPresentPeople + 1)
        val personDao = "Sometime".let {
            personRepository.findBySurname(surname = it).firstOrNull() ?: fail { "Person with surname $it not found" }
        }

        assertAll {
            assertThat(personDao.addressDao).isEqualTo(personToPersist.addressDao)
            assertThat(personDao.description).isEqualTo("Me, myself, and I")
            assertThat(personDao.locale).isEqualTo("no_NO")
            assertThat(personDao.firstName).isEqualTo("Born")
        }
    }

    @Test
    fun `should save then update and read a person entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val personToPersist = initPerson(
            address = address,
            firstName = "B",
            description = "Just me...",
            locale = "no_NO",
            surname = "Mine",
        )

        personRepository.save(personDao = personToPersist.toPersonDao())

        val personDao = ("Mine" to "Cula").let {
            val mine = personRepository.findBySurname(surname = it.first)
            val person = mine.firstOrNull() ?: fail { "Person with surname ${it.first} not found" }

            person.description = "There is no try"
            person.locale = "dk_DK"
            person.firstName = "Dr. A."
            person.surname = it.second

            personRepository.save(personDao = person)
            val foundCula = personRepository.findBySurname(surname = it.second)

            foundCula.firstOrNull()
        }

        assertAll {
            assertThat(personDao?.description).isEqualTo("There is no try")
            assertThat(personDao?.locale).isEqualTo("dk_DK")
            assertThat(personDao?.firstName).isEqualTo("Dr. A.")
            assertThat(personDao?.users)
        }
    }

    @Test
    fun `should be able to relate a user`() {
        val adder = "Adder"
        val alreadyPresentPeople = personRepository.findAll().count()
        val address = initAddress(
            persistent = Persistent(id = UUID.randomUUID()),
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()),
            surname = adder,
        )

        personRepository.save(personDao = person.toPersonDao())

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE,
        )

        userRepository.save(user = user.toUserDao())

        assertThat(personRepository.findAll()).hasSize(alreadyPresentPeople + 1)
        val personDao = personRepository.findBySurname(surname = adder).first()

        personDao.users.let {
            assertThat(it).hasSize(1)

            val persistedUser = personDao.users.firstOrNull()

            assertAll {
                assertThat(persistedUser?.emailAddress).isEqualTo("public@services.com")
                assertThat(persistedUser?.username).isEqualTo("black")
            }
        }
    }
}
