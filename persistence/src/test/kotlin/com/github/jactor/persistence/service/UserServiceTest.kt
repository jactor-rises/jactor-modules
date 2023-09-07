package com.github.jactor.persistence.service

import com.github.jactor.persistence.command.CreateUserCommand
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.dto.UserInternalDto.Usertype
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.persistence.entity.UserEntity.Companion.aUser
import com.github.jactor.persistence.repository.PersonRepository
import com.github.jactor.persistence.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
internal class UserServiceTest {

    @Autowired
    private lateinit var userServiceToTest: UserService

    @MockkBean
    private lateinit var personRepository: PersonRepository

    @MockkBean
    private lateinit var userRepositoryMock: UserRepository

    @Test
    fun `should map a user entity to a dto`() {
        val addressDto = AddressInternalDto()
        val personDto = PersonInternalDto()

        personDto.address = addressDto

        every { userRepositoryMock.findByUsername("jactor") } returns Optional.of(
            aUser(
                UserInternalDto(
                    PersistentDto(),
                    personDto,
                    null,
                    "jactor",
                    Usertype.ACTIVE
                )
            )
        )

        val user = userServiceToTest.find("jactor") ?: throw AssertionError("mocking?")

        assertAll(
            { assertThat(user).`as`("user").isNotNull() },
            { assertThat(user.username).`as`("user.username").isEqualTo("jactor") }
        )
    }

    @Test
    fun `should also map a user entity to a dto when finding by id`() {
        val addressDto = AddressInternalDto()
        val personDto = PersonInternalDto()
        personDto.address = addressDto

        every { userRepositoryMock.findById(69L) } returns Optional.of(
            aUser(UserInternalDto(PersistentDto(), personDto, null, "jactor", Usertype.ACTIVE))
        )

        val user = userServiceToTest.find(69L) ?: throw AssertionError("mocking?")

        assertAll(
            { assertThat(user).`as`("user").isNotNull() },
            { assertThat(user.username).`as`("user.username").isEqualTo("jactor") }
        )
    }

    @Test
    fun `should update a UserDto with an UserEntity`() {
        val userDto = UserInternalDto()
        userDto.id = 1L
        userDto.username = "marley"

        val persistentDto = PersistentDto(
            1L, "", LocalDateTime.now().minusMonths(1), "", LocalDateTime.now().minusDays(1)
        )

        every { userRepositoryMock.findById(1L) } returns Optional.of(
            UserEntity(UserInternalDto(persistentDto, userDto))
        )

        val user = userServiceToTest.update(userDto)
        assertThat(user?.username).isEqualTo("marley")
    }

    @Test
    fun `should create and save person for the user`() {
        val createUserCommand = CreateUserCommand("jactor", "Jacobsen")
        val userDto = UserInternalDto()
        val userEntity = UserEntity(userDto)
        val personEntitySlot = slot<PersonEntity>()

        every { userRepositoryMock.save(any()) } returns userEntity
        every { personRepository.save(capture(personEntitySlot)) } returns PersonEntity(PersonInternalDto())

        val user = userServiceToTest.create(createUserCommand)

        assertAll(
            { assertThat(user).`as`("user").isEqualTo(userDto) },
            { assertThat(personEntitySlot.captured).`as`("person to save").isNotNull }
        )
    }
}
