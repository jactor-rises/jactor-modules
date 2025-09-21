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
import com.github.jactor.persistence.test.initUser
import com.github.jactor.shared.api.CreateUserCommand
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.test.runTest

internal class UserServiceTest @Autowired constructor(
    private val userServiceToTest: UserService,
    @MockkBean private val personRepositoryMockk: PersonRepository,
    @MockkBean private val userRepositoryMockk: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should map a user entity to a dto`() = runTest {
        val addressDto = initAddress()
        val personDto = initPerson(address = addressDto)

        every { userRepositoryMockk.findByUsername("jactor") } returns Optional.of(
            initUser(
                person = personDto,
                emailAddress = null,
                username = "jactor",
                usertype = User.Usertype.ACTIVE
            ).toUserDao()
        )

        val user = userServiceToTest.find("jactor") ?: throw AssertionError("mocking?")

        assertAll {
            assertThat(user).isNotNull()
            assertThat(user.username).isEqualTo("jactor")
        }
    }

    @Test
    fun `should also map a user entity to a dto when finding by id`() = runTest {
        val uuid = UUID.randomUUID()
        val addressDto = initAddress()
        val personDto = initPerson(address = addressDto)

        every { userRepositoryMockk.findById(uuid) } returns Optional.of(
            initUser(
                person = personDto,
                emailAddress = null,
                username = "jactor",
                usertype = User.Usertype.ACTIVE
            ).toUserDao()
        )

        val user = userServiceToTest.find(uuid) ?: throw AssertionError("mocking?")

        assertAll {
            assertThat(user).isNotNull()
            assertThat(user.username).isEqualTo("jactor")
        }
    }

    @Test
    fun `should update a UserDto with an UserEntity`() = runTest {
        val uuid = UUID.randomUUID()
        val user = initUser(persistent = Persistent(id = uuid), username = "marley")
        val persistent = Persistent(
            createdBy = "",
            id = uuid,
            modifiedBy = "",
            timeOfCreation = LocalDateTime.now().minusMonths(1),
            timeOfModification = LocalDateTime.now().minusDays(1)
        )

        every { userRepositoryMockk.findById(uuid) } returns Optional.of(
            UserDao(User(persistent, user))
        )

        val updatedUser = userServiceToTest.update(user)
        assertThat(updatedUser?.username).isEqualTo("marley")
    }

    @Test
    fun `should create and save person for the user`() = runTest {
        val createUserCommand = CreateUserCommand(username = "jactor", surname = "Jacobsen")
        val user = initUser()
        val userEntity = UserDao(user)
        val personEntitySlot = slot<PersonDao>()

        every { userRepositoryMockk.save(any()) } returns userEntity
        every { personRepositoryMockk.insertOrUpdate(capture(personEntitySlot)) } returns PersonDao(initPerson())

        val userCreated = userServiceToTest.create(createUserCommand)

        assertAll {
            assertThat(userCreated).isEqualTo(user)
            assertThat(personEntitySlot.captured).isNotNull()
        }
    }
}