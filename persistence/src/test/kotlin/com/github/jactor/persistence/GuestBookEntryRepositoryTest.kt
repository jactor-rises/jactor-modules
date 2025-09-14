package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookEntryRepositoryTest @Autowired constructor(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save then read guest book entry entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        ).withId()

        val savedUser = userRepository.save(UserDao(user))
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toPerson()
        )

        savedUser.guestBook = guestBook.withId().toEntity()

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)
        val guestBookEntry = initGuestBookEntry(
            guestBook = savedUser.guestBook?.toPerson(),
            creatorName = "Harry",
            entry = "Draco Dormiens Nunquam Tittilandus"
        ).withId().toEntity()

        flush { guestBookEntryRepository.save(guestBookEntry) }

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
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        ).withId()

        val savedUser = userRepository.save(UserDao(user))
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toPerson()
        )

        val savedGuestBook = guestBookRepository.save(guestBook.withId().toEntity())

        flush {
            guestBookEntryRepository.save(
                initGuestBookEntry(
                    creatorName = "Harry",
                    entry = "Draco Dormiens Nunquam Tittilandus",
                    guestBook = savedGuestBook.toPerson(),
                ).withId().toEntity()
            )
        }

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(entriesByGuestBook).hasSize(1)
        entriesByGuestBook.iterator().next().modify("Willie", "On the road again")

        flush { guestBookEntryRepository.save(entriesByGuestBook.iterator().next()) }

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
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        ).withId()

        val savedUser = userRepository.save(UserDao(user))
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toPerson()
        )

        val savedGuestBook = guestBookRepository.save(guestBook.withId().toEntity())
        guestBookEntryRepository.save(
            initGuestBookEntry(
                creatorName = "somone",
                entry = "jadda",
                guestBook = savedGuestBook.toPerson(),
            ).withId().toEntity()
        )

        val anotherUser = initUser(
            person = person,
            emailAddress = "hidden@tantooine.com",
            username = "hidden"
        ).withId()

        userRepository.save(UserDao(anotherUser))

        val anotherSavedGuestBook = guestBookRepository.save(guestBook.withId().toEntity())
        val anotherEntry = initGuestBookEntry(
            guestBook = anotherSavedGuestBook.toPerson(),
            creatorName = "shrek",
            entry = "far far away"
        ).withId()

        flush { guestBookEntryRepository.save(anotherEntry.toEntity()) }

        val lastEntry = guestBookRepository.findAll().toList()
            .flatMap { it.entries }
            .firstOrNull { it.id == anotherEntry.id }


        assertAll {
            assertThat(lastEntry).isNotNull()
            assertThat(lastEntry?.creatorName).isEqualTo("shrek")
            assertThat(lastEntry?.entry).isEqualTo("far far away")
        }
    }
}
