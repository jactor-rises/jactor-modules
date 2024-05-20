package com.github.jactor.persistence.repository

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.entity.UserBuilder
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class PersonRepositoryTest: AbstractSpringBootNoDirtyContextTest() {
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
        val address = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevar 1", city = "Testington"
            )
        ).addressInternalDto

        val personToPersist = PersonBuilder.new(
            personInternalDto = PersonInternalDto(
                address = address,
                locale = "no_NO",
                firstName = "Born",
                surname = "Sometime",
                description = "Me, myself, and I"
            )
        ).build()

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
        val addressInternalDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personToPersist = PersonBuilder.new(
            PersonInternalDto(
                address = addressInternalDto,
                locale = "no_NO",
                firstName = "B",
                surname = "Mine",
                description = "Just me..."
            )
        ).build()

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
        val alreadyPresentPeople = personRepository.findAll().count()
        val addressInternalDto = AddressInternalDto(
            persistentDto = PersistentDto(UUID.randomUUID()),
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val personInternalDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressInternalDto, surname = "Adder"
        )

        val userInternalDto = UserInternalDto(
            PersistentDto(id = UUID.randomUUID()),
            personInternalDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val userEntity = UserBuilder.new(userDto = userInternalDto).build()
        val personToPersist = userEntity.fetchPerson()

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
}