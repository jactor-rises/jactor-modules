package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initAddressDao
import com.github.jactor.persistence.test.initPersonDao
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired

internal class PersonRepositoryTest @Autowired constructor(
    private val addressRepository: AddressRepository,
    private val personRepository: PersonRepository,
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
        val addressId = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington")
        ).persistent.id ?: fail { "not persisted?!!!" }

        val addressDao = addressRepository.findById(id = addressId) ?: fail { "Address (id=$addressId) not found???" }
        val allreadyPresentPeople = personRepository.findAll().count()
        val personToPersist = PersonDao(
            addressId = addressDao.id,
            description = "Me, myself, and I",
            firstName = "Born",
            locale = "no_NO",
            surname = "Sometime",
        )

        personRepository.save(personToPersist)

        val people = personRepository.findAll()
        assertThat(people, "allready present people").hasSize(allreadyPresentPeople + 1)

        val personDao = personToPersist.surname.let {
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
        val addressId = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington")
        ).persistent.id ?: fail { "not persisted?!!!" }

        val addressDao = addressRepository.findById(id = addressId) ?: fail { "Address (id=$addressId) not found???" }

        personRepository.save(
            personDao = PersonDao(
                addressId = addressDao.id,
                firstName = "B",
                description = "Just me...",
                locale = "no_NO",
                surname = "Mine",
            )
        )

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
        }
    }
}
