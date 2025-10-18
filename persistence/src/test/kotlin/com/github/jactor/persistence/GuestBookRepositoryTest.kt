package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired

internal class GuestBookRepositoryTest @Autowired constructor(
    private val guestBookRepository: GuestBookRepository
) : AbstractSpringBootNoDirtyContextTest() {

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

        val guestBookEntity = guestBookRepository.findGuestBookByUserId(id = user.id)

        assertAll {
            assertThat(guestBookEntity?.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity?.user).isNotNull()
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
                entries = emptySet(),
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
}
