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
            personInternal = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val userEntity = userRepository.save(user.toUserDao())

        userEntity.guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = userEntity.toPerson()
        ).toEntity()

        flush { }

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

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
            personInternal = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val userEntity = userRepository.save(user.toUserDao())

        userEntity.guestBook = initGuestBook(
            entries = emptySet(),
            title = "home sweet home",
            user = userEntity.toPerson()
        ).toEntity()

        flush { guestBookRepository.save(userEntity.guestBook ?: fail(message = "User missing guest book")) }

        val guestBookEntityToUpdate = guestBookRepository.findByUser(userEntity)

        guestBookEntityToUpdate!!.title = "5000 thousands miles away from home"

        flush { guestBookRepository.save(guestBookEntityToUpdate) }

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertThat(guestBookEntity!!.title).isEqualTo("5000 thousands miles away from home")
    }
}
