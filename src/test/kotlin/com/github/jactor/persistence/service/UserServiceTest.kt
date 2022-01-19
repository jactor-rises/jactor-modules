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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest
internal class UserServiceTest {

    @Autowired
    private lateinit var userServiceToTest: UserService

    @MockBean
    private lateinit var personRepository: PersonRepository

    @MockBean
    private lateinit var userRepositoryMock: UserRepository

    @Test
    fun `should map a user entity to a dto`() {
        val addressDto = AddressInternalDto()
        val personDto = PersonInternalDto()

        personDto.address = addressDto

        whenever(userRepositoryMock.findByUsername("jactor"))
            .thenReturn(Optional.of(aUser(UserInternalDto(PersistentDto(), personDto, null, "jactor", Usertype.ACTIVE))))

        val user = userServiceToTest.find("jactor").orElseThrow { AssertionError("mocking?") }

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

        whenever(userRepositoryMock.findById(69L))
            .thenReturn(Optional.of(aUser(UserInternalDto(PersistentDto(), personDto, null, "jactor", Usertype.ACTIVE))))

        val user = userServiceToTest.find(69L).orElseThrow { AssertionError("mocking?") }

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

        val persistentDto = PersistentDto(1L, "", LocalDateTime.now().minusMonths(1), "", LocalDateTime.now().minusDays(1))

        whenever(userRepositoryMock.findById(1L)).thenReturn(Optional.of(UserEntity(UserInternalDto(persistentDto, userDto))))

        val optionalUser = userServiceToTest.update(userDto)
        assertThat(optionalUser).isPresent.get().extracting(UserInternalDto::username).isEqualTo("marley")
    }

    @Test
    fun `should create and save person for the user`() {
        val createUserCommand = CreateUserCommand("jactor", "Jacobsen")
        val userDto = UserInternalDto()
        val userEntity = UserEntity(userDto)

        Mockito.`when`(userRepositoryMock.save(ArgumentMatchers.any())).thenReturn(userEntity)
        Mockito.`when`(personRepository.save(ArgumentMatchers.any())).thenReturn(PersonEntity(PersonInternalDto()))

        val user = userServiceToTest.create(createUserCommand)

        assertAll(
            { assertThat(user).`as`("user").isEqualTo(userDto) },
            {
                val personCaptor = ArgumentCaptor.forClass(PersonEntity::class.java)
                Mockito.verify(personRepository).save(personCaptor.capture())
                assertThat(personCaptor.value).`as`("person to save").isNotNull()
            }
        )
    }
}
