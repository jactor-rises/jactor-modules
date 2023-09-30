package com.github.jactor.persistence.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.entity.PersonEntity.Companion.aPerson
import com.github.jactor.persistence.entity.UserEntity.Companion.aUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@SpringBootTest
@Transactional
internal class PersonRepositoryTest {

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should find default persons`() {
        val personEntities = personRepository.findBySurname("Jacobsen")
        val firstNames: MutableList<String> = ArrayList()

        for (personEntity in personEntities) {
            firstNames.add(personEntity.firstName ?: "unknown")
        }

        assertThat(firstNames).containsAll("Tor Egil", "Suthatip")
    }

    @Test
    fun `should save then read a person entity`() {
        val allreadyPresentPeople = numberOf(personRepository.findAll())
        val address = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington")
        val personToPersist = aPerson(
            PersonInternalDto(
                address = address,
                locale = "no_NO",
                firstName = "Born",
                surname = "Sometime",
                description = "Me, myself, and I"
            )
        )

        personRepository.save(personToPersist)
        entityManager.flush()
        entityManager.clear()

        val people = personRepository.findAll().toList()
        assertThat(people).hasSize(allreadyPresentPeople + 1)
        val personEntity = personRepository.findBySurname("Sometime").iterator().next()

        assertAll {
            assertThat(personEntity.addressEntity).isEqualTo(personToPersist.addressEntity)
            assertThat(personEntity.description).isEqualTo("Me, myself, and I")
            assertThat(personEntity.locale).isEqualTo("no_NO")
            assertThat(personEntity.firstName).isEqualTo("Born")
        }
    }

    @Test
    fun `should save then update and read a person entity`() {
        val addressInternalDto =
            AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        val personToPersist = aPerson(
            PersonInternalDto(
                address = addressInternalDto,
                locale = "no_NO",
                firstName = "B",
                surname = "Mine",
                description = "Just me..."
            )
        )

        personRepository.save(personToPersist)
        entityManager.flush()
        entityManager.clear()

        val mine = personRepository.findBySurname("Mine")
        val person = mine.iterator().next()

        person.description = "There is no try"
        person.locale = "dk_DK"
        person.firstName = "Dr. A."
        person.surname = "Cula"

        personRepository.save(person)
        entityManager.flush()
        entityManager.clear()

        val foundCula = personRepository.findBySurname("Cula")
        val personEntity = foundCula.iterator().next()

        assertAll {
            assertThat(personEntity.description).isEqualTo("There is no try")
            assertThat(personEntity.locale).isEqualTo("dk_DK")
            assertThat(personEntity.firstName).isEqualTo("Dr. A.")
            assertThat(personEntity.getUsers()).isEqualTo(person.getUsers())
        }
    }

    @Test
    fun `should be able to relate a user`() {
        val alreadyPresentPeople = numberOf(personRepository.findAll())
        val addressInternalDto =
            AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personInternalDto = PersonInternalDto(address = addressInternalDto, surname = "Adder")
        val userInternalDto = UserInternalDto(
            PersistentDto(),
            personInternalDto,
            emailAddress = "public@services.com",
            username = "black"
        )
        val userEntity = aUser(userInternalDto)
        val personToPersist = userEntity.fetchPerson()!!

        personRepository.save<PersonEntity>(personToPersist)
        entityManager.flush()
        entityManager.clear()

        assertThat(personRepository.findAll().toList()).hasSize(alreadyPresentPeople + 1)
        val personEntity = personRepository.findBySurname("Adder").iterator().next()
        assertThat(personEntity.getUsers()).hasSize(1)
        val persistedUser = personEntity.getUsers().iterator().next()

        assertAll {
            assertThat(persistedUser.emailAddress).isEqualTo("public@services.com")
            assertThat(persistedUser.username).isEqualTo("black")
        }
    }

    private fun numberOf(people: Iterable<PersonEntity?>): Int {
        var counter = 0
        val peopleIterator = people.iterator()

        while (peopleIterator.hasNext()) {
            peopleIterator.next()
            counter++
        }

        return counter
    }
}