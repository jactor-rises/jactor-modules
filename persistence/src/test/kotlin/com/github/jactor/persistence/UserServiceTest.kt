package com.github.jactor.persistence

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initCreateUserCommand
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.persistence.util.toCreateUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

internal class UserServiceTest {
    private val personRepositoryMockk: PersonRepository = mockk {}
    private val userRepositoryMockk: UserRepository = mockk {}
    private val userServiceToTest = UserService(
        userRepository = userRepositoryMockk,
    ).also {
        JactorPersistenceRepositiesConfig.fetchPersonRelation = { id -> personRepositoryMockk.findById(id = id) }
        JactorPersistenceRepositiesConfig.fetchUserRelation = { id -> userRepositoryMockk.findById(id = id) }
    }

    @Test
    fun `should map a user entity to a dto`() = runTest {
        val addressDto = initAddress()
        val personDto = initPerson(address = addressDto)

        every { userRepositoryMockk.findByUsername("jactor") } returns initUser(
            person = personDto,
            emailAddress = null,
            username = "jactor",
            usertype = User.Usertype.ACTIVE
        ).toUserDao()

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

        every { userRepositoryMockk.findById(uuid) } returns initUser(
            person = personDto,
            emailAddress = null,
            username = "jactor",
            usertype = User.Usertype.ACTIVE
        ).toUserDao()

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

        every { userRepositoryMockk.findById(id = uuid) } returns user.toUserDao()
        every { userRepositoryMockk.save(any()) } returns user.toUserDao()

        val updatedUser = userServiceToTest.update(user)

        assertThat(updatedUser.username).isEqualTo("marley")
    }

    @Test
    fun `should create and save person for the user`() = runTest {
        val createUserCommand = initCreateUserCommand(
            personId = UUID.randomUUID(),
            username = "jactor",
        )

        every { userRepositoryMockk.save(user = any()) } returns initUserDao(createUserCommand = createUserCommand)
        every { personRepositoryMockk.findById(id = createUserCommand.personId!!) } returns mockk {
            every { toPerson() } returns initPerson(surname = "Jacobsen")
        }

        val user = userServiceToTest.create(createUser = createUserCommand.toCreateUser())

        assertAll {
            assertThat(user.person?.surname).isEqualTo("Jacobsen")
            assertThat(user.username).isEqualTo("jactor")
        }
    }
}
