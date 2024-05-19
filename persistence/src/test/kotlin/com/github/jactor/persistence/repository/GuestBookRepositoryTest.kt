package com.github.jactor.persistence.repository

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.GuestBookBuilder
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import jakarta.persistence.EntityManager

@SpringBootTest
@Transactional
internal class GuestBookRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestBookRepository: GuestBookRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should write then read guest book`() {
        val addressDto = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testington"
                )
            )
            .addressInternalDto

        val personDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressDto, surname = "AA"
        )

        val userDto = UserInternalDto(
            PersistentDto(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        )

        val userEntity = userRepository.save(
            UserBuilder.new(userDto = userDto).build()
        )

        userEntity.setGuestBook(
            GuestBookBuilder.new(
                GuestBookDto(
                    entries = emptySet(),
                    title = "home sweet home",
                    userInternal = userEntity.asDto()
                )
            ).buildGuestBookEntity()
        )

        entityManager.flush()
        entityManager.clear()

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertAll {
            assertThat(guestBookEntity?.title).isEqualTo("home sweet home")
            assertThat(guestBookEntity?.user).isNotNull()
        }
    }

    @Test
    fun `should write then update and read guest book`() {
        val addressDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personDto = PersonBuilder.new(PersonInternalDto(address = addressDto, surname = "AA")).personInternalDto
        val userDto = UserBuilder.unchanged(
            userInternalDto = UserInternalDto(
                persistentDto = PersistentDto(),
                personInternal = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val userEntity = userRepository.save(UserBuilder.new(userDto).build())

        userEntity.setGuestBook(
            GuestBookBuilder.new(
                GuestBookDto(
                    entries = emptySet(),
                    title = "home sweet home",
                    userInternal = userEntity.asDto()
                )
            ).buildGuestBookEntity()
        )

        guestBookRepository.save(userEntity.guestBook!!)
        entityManager.flush()
        entityManager.clear()

        val guestBookEntityToUpdate = guestBookRepository.findByUser(userEntity)

        guestBookEntityToUpdate!!.title = "5000 thousands miles away from home"

        guestBookRepository.save(guestBookEntityToUpdate)
        entityManager.flush()
        entityManager.clear()

        val guestBookEntity = guestBookRepository.findByUser(userEntity)

        assertThat(guestBookEntity!!.title).isEqualTo("5000 thousands miles away from home")
    }
}
