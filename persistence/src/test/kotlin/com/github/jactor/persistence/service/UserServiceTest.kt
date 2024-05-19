package com.github.jactor.persistence.service

import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.github.jactor.persistence.command.CreateUserCommand
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.dto.UserInternalDto.Usertype
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.entity.UserBuilder
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.persistence.repository.PersonRepository
import com.github.jactor.persistence.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.slot

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
            UserBuilder.new(
                userDto = UserInternalDto(
                    person = personDto,
                    emailAddress = null,
                    username = "jactor",
                    usertype = Usertype.ACTIVE
                )
            ).build()
        )

        val user = userServiceToTest.find("jactor") ?: throw AssertionError("mocking?")

        assertAll {
            assertThat(user).isNotNull()
            assertThat(user.username).isEqualTo("jactor")
        }
    }

    @Test
    fun `should also map a user entity to a dto when finding by id`() {
        val uuid = UUID.randomUUID()
        val addressDto = AddressInternalDto()
        val personDto = PersonInternalDto()
        personDto.address = addressDto

        every { userRepositoryMock.findById(uuid) } returns Optional.of(
            UserBuilder.new(
                UserInternalDto(
                    person = personDto,
                    emailAddress = null,
                    username = "jactor",
                    usertype = Usertype.ACTIVE
                )
            ).build()
        )

        val user = userServiceToTest.find(uuid) ?: throw AssertionError("mocking?")

        assertAll {
            assertThat(user).isNotNull()
            assertThat(user.username).isEqualTo("jactor")
        }
    }

    @Test
    fun `should update a UserDto with an UserEntity`() {
        val uuid = UUID.randomUUID()
        val userDto = UserInternalDto()
        userDto.id = uuid
        userDto.username = "marley"

        val persistentDto = PersistentDto(
            uuid, "", LocalDateTime.now().minusMonths(1), "", LocalDateTime.now().minusDays(1)
        )

        every { userRepositoryMock.findById(uuid) } returns Optional.of(
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

        assertAll {
            assertThat(user).isEqualTo(userDto)
            assertThat(personEntitySlot.captured).isNotNull()
        }
    }
}
