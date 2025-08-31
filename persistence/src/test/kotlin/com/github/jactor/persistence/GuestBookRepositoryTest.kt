package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
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
        val addressDto = AddressBuilder
            .new(
                address = Address(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testington"
                )
            )
            .address

        val personDto = Person(
            persistent = Persistent(id = UUID.randomUUID()), address = addressDto, surname = "AA"
        )

        val userDto = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val userEntity = userRepository.save(
            UserBuilder.new(userDto = userDto).build()
        )

        userEntity.guestBook = GuestBookBuilder.new(
            GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = userEntity.toModel()
            )
        ).buildGuestBookEntity()

        flush { }

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertAll {
            assertThat(guestBookEntity?.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity?.user).isNotNull()
        }
    }

    @Test
    fun `should write then update and read guest book`() {
        val addressDto = AddressBuilder.new(
            address = Address(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).address

        val personDto = PersonBuilder.new(Person(address = addressDto, surname = "AA")).person
        val userDto = UserBuilder.unchanged(
            user = User(
                persistent = Persistent(),
                personInternal = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val userEntity = userRepository.save(UserBuilder.new(userDto).build())

        userEntity.guestBook = GuestBookBuilder.new(
            GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = userEntity.toModel()
            )
        ).buildGuestBookEntity()

        flush { guestBookRepository.save(userEntity.guestBook ?: fail(message = "User missing guest book")) }

        val guestBookEntityToUpdate = guestBookRepository.findByUser(userEntity)

        guestBookEntityToUpdate!!.title = "5000 thousands miles away from home"

        flush { guestBookRepository.save(guestBookEntityToUpdate) }

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertThat(guestBookEntity!!.title).isEqualTo("5000 thousands miles away from home")
    }
}
