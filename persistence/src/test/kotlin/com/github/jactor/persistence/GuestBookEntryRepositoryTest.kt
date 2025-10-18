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
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save then read guest book entry entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "AA")
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        ).toUserDao()

        val savedUser = userRepository.save(userDao = user)
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toUser()
        )

        val savedGuestBook = guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao())
        val guestBookEntry = initGuestBookEntry(
            guestBook = savedGuestBook.toGuestBook(),
            creatorName = "Harry",
            entry = "Draco Dormiens Nunquam Tittilandus"
        ).toGuestBookEntryDao()

        guestBookRepository.save(guestBookEntryDao = guestBookEntry)

        val entriesByGuestBook = guestBookRepository.findGuestBookEtriesByGuestBookId(id = savedGuestBook.id!!)
        assertThat(entriesByGuestBook).hasSize(1)
        val entry = entriesByGuestBook.first()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Harry")
            assertThat(entry.entry).isEqualTo("Draco Dormiens Nunquam Tittilandus")
        }
    }

    @Test
    fun `should save then modify and read guest book entry entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "AA")
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val savedUser = userRepository.save(userDao = user.toUserDao())
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toUser()
        )

        val savedGuestBook = guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao())

        guestBookRepository.save(
            guestBookEntryDao = initGuestBookEntry(
                creatorName = "Harry",
                entry = "Draco Dormiens Nunquam Tittilandus",
                guestBook = savedGuestBook.toGuestBook(),
            ).toGuestBookEntryDao()
        )

        val entriesByGuestBook = guestBookRepository.findGuestBookEtriesByGuestBookId(id = savedGuestBook.id!!)
        assertThat(entriesByGuestBook).hasSize(1)
        entriesByGuestBook.first().apply { entry = "On the road again" }.modifiedBy(modifier = "Willie")

        guestBookRepository.save(guestBookEntryDao = entriesByGuestBook.first())

        val modifiedEntriesByGuestBook = guestBookRepository.findGuestBookEtriesByGuestBookId(
            id = savedGuestBook.id!!
        )

        assertThat(modifiedEntriesByGuestBook).hasSize(1)
        val entry = modifiedEntriesByGuestBook.first()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Willie")
            assertThat(entry.entry).isEqualTo("On the road again")
        }
    }

    @Test
    fun `should write two entries to two different guest books and then find one entry`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "AA")
        val user = initUser(
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val savedUser = userRepository.save(userDao = user.toUserDao())
        val guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = savedUser.toUser()
        )

        val savedGuestBook = guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao())
        guestBookRepository.save(
            initGuestBookEntry(
                creatorName = "somone",
                entry = "jadda",
                guestBook = savedGuestBook.toGuestBook(),
            ).toGuestBookEntryDao()
        )

        val anotherUser = initUser(
            person = person,
            emailAddress = "hidden@tantooine.com",
            username = "hidden"
        )

        userRepository.save(userDao = anotherUser.toUserDao())

        val anotherSavedGuestBook = guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao())
        val anotherEntry = initGuestBookEntry(
            guestBook = anotherSavedGuestBook.toGuestBook(),
            creatorName = "shrek",
            entry = "far far away"
        )

        guestBookRepository.save(guestBookEntryDao = anotherEntry.toGuestBookEntryDao())

        val lastEntry = guestBookRepository.findAllGuestBooks()
            .flatMap { it.entries }
            .firstOrNull { it.id == anotherEntry.id }

        assertAll {
            assertThat(lastEntry).isNotNull()
            assertThat(lastEntry?.creatorName).isEqualTo("shrek")
            assertThat(lastEntry?.entry).isEqualTo("far far away")
        }
    }
}
