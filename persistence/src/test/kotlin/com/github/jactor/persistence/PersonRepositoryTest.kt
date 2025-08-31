package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class PersonRepositoryTest @Autowired constructor(
    private val personRepository: PersonRepository
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
        val address = Address(
            zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington"
        ).withId()

        val personToPersist = PersonBuilder.new(
            person = Person(
                address = address,
                locale = "no_NO",
                firstName = "Born",
                surname = "Sometime",
                description = "Me, myself, and I"
            )
        ).build()

        flush { personRepository.save(personToPersist) }

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
        val address = Address(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val personToPersist = PersonBuilder.new(
            Person(
                address = address,
                locale = "no_NO",
                firstName = "B",
                surname = "Mine",
                description = "Just me..."
            )
        ).build()

        flush { personRepository.save(personToPersist) }

        val mine = personRepository.findBySurname("Mine")
        val person = mine.iterator().next()

        person.description = "There is no try"
        person.locale = "dk_DK"
        person.firstName = "Dr. A."
        person.surname = "Cula"

        flush { personRepository.save(person) }

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
        val alreadyPresentPeople = personRepository.findAll().count()
        val address = Address(
            persistent = Persistent(id = UUID.randomUUID()),
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val person = Person(
            persistent = Persistent(id = UUID.randomUUID()), address = address, surname = "Adder"
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            person,
            emailAddress = "public@services.com",
            username = "black"
        )

        val userEntity = UserBuilder.new(userDto = user).build()
        val personToPersist = userEntity.fetchPerson()

        flush { personRepository.save<PersonEntity>(personToPersist) }

        assertThat(personRepository.findAll().toList()).hasSize(alreadyPresentPeople + 1)
        val personEntity = personRepository.findBySurname("Adder").iterator().next()
        assertThat(personEntity.getUsers()).hasSize(1)
        val persistedUser = personEntity.getUsers().iterator().next()

        assertAll {
            assertThat(persistedUser.emailAddress).isEqualTo("public@services.com")
            assertThat(persistedUser.username).isEqualTo("black")
        }
    }
}