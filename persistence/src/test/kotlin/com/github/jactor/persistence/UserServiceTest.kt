package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.shared.api.CreateUserCommand
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.slot

internal class UserServiceTest @Autowired constructor(
    private val userServiceToTest: UserService,
    @MockkBean private val personRepositoryMockk: PersonRepository,
    @MockkBean private val userRepositoryMockk: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should map a user entity to a dto`() {
        val addressDto = initAddress()
        val personDto = initPerson(address = addressDto,)

        every { userRepositoryMockk.findByUsername("jactor") } returns Optional.of(
            UserBuilder.new(
                userDto = User(
                    person = personDto,
                    emailAddress = null,
                    username = "jactor",
                    usertype = User.Usertype.ACTIVE
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
        val addressDto = initAddress()
        val personDto = initPerson(address = addressDto,)

        every { userRepositoryMockk.findById(uuid) } returns Optional.of(
            UserBuilder.new(
                User(
                    person = personDto,
                    emailAddress = null,
                    username = "jactor",
                    usertype = User.Usertype.ACTIVE
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
        val userDto = User(
            persistent = Persistent(id = uuid),
            username = "marley"
        )

        val persistent = Persistent(
            createdBy = "",
            id = uuid,
            modifiedBy = "",
            timeOfCreation = LocalDateTime.now().minusMonths(1),
            timeOfModification = LocalDateTime.now().minusDays(1)
        )

        every { userRepositoryMockk.findById(uuid) } returns Optional.of(
            UserEntity(User(persistent, userDto))
        )

        val user = userServiceToTest.update(userDto)
        assertThat(user?.username).isEqualTo("marley")
    }

    @Test
    fun `should create and save person for the user`() {
        val createUserCommand = CreateUserCommand(username = "jactor", surname = "Jacobsen")
        val user = User()
        val userEntity = UserEntity(user)
        val personEntitySlot = slot<PersonEntity>()

        every { userRepositoryMockk.save(any()) } returns userEntity
        every { personRepositoryMockk.save(capture(personEntitySlot)) } returns PersonEntity(initPerson())

        val userCreated = userServiceToTest.create(createUserCommand)

        assertAll {
            assertThat(userCreated).isEqualTo(user)
            assertThat(personEntitySlot.captured).isNotNull()
        }
    }
}