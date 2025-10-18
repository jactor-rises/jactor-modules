package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import java.util.UUID

internal class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val personRepository: PersonRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should find user with username jactor`() {
        val userDao = userRepository.findByUsername("jactor")

        assertAll {
            assertThat(userDao?.emailAddress).isEqualTo("tor.egil.jacobsen@gmail.com")
            assertThat(userDao?.personDao?.firstName).isEqualTo("Tor Egil")
        }
    }

    @Test
    fun `should write then read a user entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "Solo")
        val userToPersist = initUser(
            person = person,
            emailAddress = "smuggle.fast@tantooine.com",
            username = "smuggler"
        ).toUserDao()

        userRepository.save(userToPersist)

        val userDao = userRepository.findByUsername("smuggler")

        assertAll {
            assertThat(userDao?.personDao).isEqualTo(userToPersist.personDao)
            assertThat(userDao?.username).isEqualTo("smuggler")
            assertThat(userDao?.emailAddress).isEqualTo("smuggle.fast@tantooine.com")
        }
    }

    @Test
    fun `should write then update and read a user entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val person = initPerson(address = address, surname = "AA")
        val userToPersist = initUser(
            persistent = Persistent(),
            person = person,
            emailAddress = "casuel@tantooine.com",
            username = "causual"
        ).toUserDao()

        userRepository.save(userToPersist)

        val lukewarm = "lukewarm"
        userToPersist.username = lukewarm
        userToPersist.emailAddress = "luke@force.com"

        userRepository.save(userToPersist)

        val userDao = userRepository.findByUsername(lukewarm)

        assertAll {
            assertThat(userDao?.username).isEqualTo(lukewarm)
            assertThat(userDao?.emailAddress).isEqualTo("luke@force.com")
        }
    }

    @Test
    fun `should find active users and admins`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val spidyPerson = initPerson(address = address, surname = "Parker")
        val superPerson = initPerson(address = address, surname = "Kent")
        val userDao = initUser(
            persistent = Persistent(),
            person = spidyPerson,
            emailAddress = null,
            username = "spiderman"
        ).toUserDao()

        userRepository.save(userDao)
        userRepository.save(
            initUser(
                person = superPerson,
                emailAddress = null,
                username = "superman",
                usertype = User.Usertype.INACTIVE
            ).toUserDao()
        )

        val usernames = userRepository.findUsernames(listOf(UserDao.UserType.ACTIVE, UserDao.UserType.ADMIN))

        assertThat(usernames).containsAtLeast("tip", "spiderman", "jactor")
    }

    @Test
    fun `should be able to relate a user`() {
        val adder = "Adder"
        val alreadyPresentPeople = personRepository.findAll().count()
        val address = initAddress(
            persistent = Persistent(id = UUID.randomUUID()),
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()),
            surname = adder,
        )

        personRepository.save(personDao = person.toPersonDao())

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE,
        )

        userRepository.save(user = user.toUserDao())

        assertThat(personRepository.findAll()).hasSize(alreadyPresentPeople + 1)
        val personDao = personRepository.findBySurname(surname = adder).first()

        personDao.users.let {
            assertThat(it).hasSize(1)

            val persistedUser = personDao.users.firstOrNull()

            assertAll {
                assertThat(persistedUser?.emailAddress).isEqualTo("public@services.com")
                assertThat(persistedUser?.username).isEqualTo("black")
            }
        }
    }
}
