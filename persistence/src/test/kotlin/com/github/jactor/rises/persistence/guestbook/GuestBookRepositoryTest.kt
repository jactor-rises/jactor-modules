package com.github.jactor.rises.persistence.guestbook

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.github.jactor.rises.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.rises.persistence.test.initAddress
import com.github.jactor.rises.persistence.test.initGuestBook
import com.github.jactor.rises.persistence.test.initGuestBookEntry
import com.github.jactor.rises.persistence.test.initPerson
import com.github.jactor.rises.persistence.test.initUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired

internal class GuestBookRepositoryTest @Autowired constructor(
    private val guestBookRepository: GuestBookRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @BeforeEach
    fun `reset fetch relations`() {
        resetFetchRelations()
    }

    @Test
    fun `should write then read guest book`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual",
            )
        )

        guestBookRepository.save(
            guestBookDao = GuestBookDao(
                title = "home sweet home",
                userId = user.id,
            )
        )

        val guestBookDao = guestBookRepository.findGuestBookByUserId(id = user.id)

        assertAll {
            assertThat(guestBookDao?.title).isEqualTo("home sweet home")
            assertThat(guestBookDao?.userId).isEqualTo(user.id)
        }
    }

    @Test
    fun `should write then update and read guest book`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual",
            )
        )

        guestBookRepository.save(
            guestBookDao = initGuestBook(
                title = "home sweet home",
                user = user,
            ).toGuestBookDao()
        )

        val guestBookDaoToUpdate = guestBookRepository.findGuestBookByUserId(id = user.id)
            ?: fail(message = "Should have found a guest book for user ${user.username}")

        guestBookDaoToUpdate.title = "5000 thousands miles away from home"

        guestBookRepository.save(guestBookDaoToUpdate)

        val guestBookDao = guestBookRepository.findGuestBookByUserId(id = user.id)
            ?: fail(message = "Should have found a guest book for user ${user.username}")

        assertThat(guestBookDao.title).isEqualTo("5000 thousands miles away from home")
    }

    @Test
    fun `should save then read dao for guest book entry`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        )

        val person = save(person = initPerson(address = address, surname = "AA"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        )

        val guestBook = save(
            guestBook = initGuestBook(
                title = "home sweet home",
                user = user,
            )
        )

        val guestBookEntry = initGuestBookEntry(
            guestBook = guestBook,
            creatorName = "Harry",
            entry = "Draco Dormiens Nunquam Tittilandus"
        ).toGuestBookEntryDao()

        guestBookRepository.save(guestBookEntryDao = guestBookEntry)

        val entriesByGuestBook = guestBookRepository.findGuestBookEtriesByGuestBookId(id = guestBook.id)
        assertThat(entriesByGuestBook).hasSize(1)
        val entry = entriesByGuestBook.firstOrNull()

        assertAll {
            assertThat(entry?.guestName).isEqualTo("Harry")
            assertThat(entry?.entry).isEqualTo("Draco Dormiens Nunquam Tittilandus")
        }
    }

    @Test
    fun `should write two entries to a guest book and then find one entry`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        )

        val person = save(person = initPerson(address = address, surname = "AA"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        )

        val guestBook = save(
            guestBook = initGuestBook(
                title = "home sweet home",
                user = user,
            )
        )

        guestBookRepository.save(
            guestBookEntryDao = initGuestBookEntry(
                creatorName = "somone",
                entry = "jadda",
                guestBook = guestBook,
            ).toGuestBookEntryDao()
        )

        val anEntry = initGuestBookEntry(
            guestBook = guestBook,
            creatorName = "shriek",
            entry = "i am out there"
        )

        val anotherEntry = initGuestBookEntry(
            guestBook = guestBook,
            creatorName = "shrek",
            entry = "far far away"
        )

        guestBookRepository.save(guestBookEntryDao = anEntry.toGuestBookEntryDao())
        guestBookRepository.save(guestBookEntryDao = anotherEntry.toGuestBookEntryDao())

        val lastEntry = guestBookRepository.findGuestBookEtriesByGuestBookId(id = guestBook.id)
            .lastOrNull()

        assertAll {
            assertThat(lastEntry, "last entry").isNotNull()
            assertThat(lastEntry?.guestName, "guest on last entry").isEqualTo("shrek")
            assertThat(lastEntry?.entry, "text from last entry").isEqualTo("far far away")
        }
    }
}
