package com.github.jactor.persistence.guestbook

import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.AddressModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.person.PersonModel
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.AddressBuilder
import com.github.jactor.persistence.person.PersonBuilder
import com.github.jactor.persistence.user.UserBuilder
import com.github.jactor.persistence.user.UserRepository
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var guestBookRepository: GuestBookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should write then read guest book`() {
        val addressDto = AddressBuilder
            .new(
                addressModel = AddressModel(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testington"
                )
            )
            .addressModel

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "AA"
        )

        val userDto = UserModel(
            PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val userEntity = userRepository.save(
            UserBuilder.new(userDto = userDto).build()
        )

        userEntity.setGuestBook(
            GuestBookBuilder.new(
                GuestBookModel(
                    entries = emptySet(),
                    title = "home sweet home",
                    user = userEntity.toModel()
                )
            ).buildGuestBookEntity()
        )

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
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personDto = PersonBuilder.new(PersonModel(address = addressDto, surname = "AA")).personModel
        val userDto = UserBuilder.unchanged(
            userModel = UserModel(
                persistentModel = PersistentModel(),
                personInternal = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val userEntity = userRepository.save(UserBuilder.new(userDto).build())

        userEntity.setGuestBook(
            GuestBookBuilder.new(
                GuestBookModel(
                    entries = emptySet(),
                    title = "home sweet home",
                    user = userEntity.toModel()
                )
            ).buildGuestBookEntity()
        )

        flush { guestBookRepository.save(userEntity.guestBook ?: fail(message = "User missing guest book")) }

        val guestBookEntityToUpdate = guestBookRepository.findByUser(userEntity)

        guestBookEntityToUpdate!!.title = "5000 thousands miles away from home"

        flush { guestBookRepository.save(guestBookEntityToUpdate) }

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertThat(guestBookEntity!!.title).isEqualTo("5000 thousands miles away from home")
    }
}
