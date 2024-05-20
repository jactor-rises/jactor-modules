package com.github.jactor.persistence.repository

import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.GuestBookBuilder
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import com.github.jactor.persistence.entity.UserEntity
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookEntryRepositoryTest: AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should save then read guest book entry entity`() {
        val addressDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personDto = PersonBuilder.new(personInternalDto = PersonInternalDto(address = addressDto, surname = "AA"))
            .personInternalDto

        val userDto = UserBuilder.new(
            userDto = UserInternalDto(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        var guestBookData = GuestBookBuilder.new(
            GuestBookDto(
                entries = emptySet(),
                title = "home sweet home",
                userInternal = savedUser.asDto()
            )
        )

        savedUser.setGuestBook(guestBookData.buildGuestBookEntity())

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)
        guestBookData = guestBookData.withEntry(
            GuestBookEntryDto(
                guestBook = savedUser.guestBook?.asDto(),
                creatorName = "Harry",
                entry = "Draco Dormiens Nunquam Tittilandus"
            )
        )

        guestBookEntryRepository.save(guestBookData.buildGuestBookEntryEntity())
        entityManager.flush()
        entityManager.clear()

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(entriesByGuestBook).hasSize(1)
        val entry = entriesByGuestBook.iterator().next()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Harry")
            assertThat(entry.entry).isEqualTo("Draco Dormiens Nunquam Tittilandus")
        }
    }

    @Test
    fun `should save then modify and read guest book entry entity`() {
        val addressDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personDto = PersonBuilder.new(personInternalDto = PersonInternalDto(address = addressDto, surname = "AA"))
            .personInternalDto

        val userDto = UserBuilder.new(
            userDto = UserInternalDto(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBookDto(
                entries = emptySet(),
                title = "home sweet home",
                userInternal = savedUser.asDto()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        guestBookEntryRepository.save(
            guestBookData.withEntry(
                GuestBookEntryDto(
                    guestBook = savedGuestBook.asDto(),
                    creatorName = "Harry",
                    entry = "Draco Dormiens Nunquam Tittilandus"
                )
            ).buildGuestBookEntryEntity()
        )

        entityManager.flush()
        entityManager.clear()

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(entriesByGuestBook).hasSize(1)
        entriesByGuestBook.iterator().next().modify("Willie", "On the road again")

        guestBookEntryRepository.save<GuestBookEntryEntity>(entriesByGuestBook.iterator().next())

        entityManager.flush()
        entityManager.clear()

        val modifiedEntriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(modifiedEntriesByGuestBook).hasSize(1)
        val entry = modifiedEntriesByGuestBook.iterator().next()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Willie")
            assertThat(entry.entry).isEqualTo("On the road again")
        }
    }

    @Test
    fun `should write two entries to two different guest books and then find one entry`() {
        val addressDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personDto = PersonBuilder.new(personInternalDto = PersonInternalDto(address = addressDto, surname = "AA"))
            .personInternalDto

        val userDto = UserBuilder.new(
            userDto = UserInternalDto(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBookDto(
                entries = emptySet(),
                title = "home sweet home",
                userInternal = savedUser.asDto()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        guestBookEntryRepository.save(
            guestBookData.withEntry(
                GuestBookEntryDto(
                    guestBook = savedGuestBook.asDto(),
                    creatorName = "somone",
                    entry = "jadda"
                )
            ).buildGuestBookEntryEntity()
        )

        val anotherUserDto = UserBuilder.new(
            userDto = UserInternalDto(
                person = personDto,
                emailAddress = "hidden@tantooine.com",
                username = "hidden"
            )
        ).userDto

        userRepository.save(UserEntity(anotherUserDto))

        val anotherGuestBookData = GuestBookBuilder.new(
            guestBookDto = GuestBookDto(
                entries = emptySet(),
                title = "home sweet home",
                userInternal = savedUser.asDto()
            )
        )

        val anotherSavedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        val anotherEntry = anotherGuestBookData.withEntry(
            guestBookEntryDto = GuestBookEntryDto(
                guestBook = anotherSavedGuestBook.asDto(),
                creatorName = "shrek",
                entry = "far far away"
            )
        ).buildGuestBookEntryEntity()

        guestBookEntryRepository.save(anotherEntry)
        entityManager.flush()
        entityManager.clear()

        val lastEntry = guestBookRepository.findAll().toList()
            .flatMap { it.getEntries() }
            .firstOrNull { it.id == anotherEntry.id }


        assertAll {
            assertThat(lastEntry).isNotNull()
            assertThat(lastEntry?.creatorName).isEqualTo("shrek")
            assertThat(lastEntry?.entry).isEqualTo("far far away")
        }
    }
}
