package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookRepositoryTest @Autowired constructor(
    private val guestBookRepository: GuestBookRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should write then read guest book`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testington"
        )

        val person = initPerson(address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "AA")
        val user = User(
            Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual",
            usertype = User.Usertype.ACTIVE
        )

        val userDao = userRepository.save(user.toUserDao())
        guestBookRepository.save(
            guestBookDao = initGuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = userDao.toUser(),
            ).toGuestBookDao()
        )

        val guestBookEntity = guestBookRepository.findGuestBookByUser(user = userDao)

        assertAll {
            assertThat(guestBookEntity?.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity?.user).isNotNull()
        }
    }

    @Test
    fun `should write then update and read guest book`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "AA")
        val user = User(
            persistent = Persistent(),
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual",
            usertype = User.Usertype.ACTIVE,
        )

        val userDao = userRepository.save(userDao = user.toUserDao())
        guestBookRepository.save(
            guestBookDao = initGuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = userDao.toUser(),
            ).toGuestBookDao()
        )

        guestBookRepository.save(userDao.guestBook ?: fail(message = "User missing guest book"))

        val guestBookDaoToUpdate = guestBookRepository.findGuestBookByUser(user = userDao)
            ?: fail(message = "Should have found a guest book for user ${userDao.username}")

        guestBookDaoToUpdate.title = "5000 thousands miles away from home"

        guestBookRepository.save(guestBookDaoToUpdate)

        val guestBookDao = guestBookRepository.findGuestBookByUser(user = userDao)
            ?: fail(message = "Should have found a guest book for user ${userDao.username}")

        assertThat(guestBookDao.title).isEqualTo("5000 thousands miles away from home")
    }
}
