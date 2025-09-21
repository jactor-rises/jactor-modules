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
import assertk.assertions.isEqualTo

internal class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should find user with username jactor`() {
        val userByName = userRepository.findByUsername("jactor")
        val userEntity = userByName.orElseThrow { userNotFound() }

        assertAll {
            assertThat(userEntity.emailAddress).isEqualTo("tor.egil.jacobsen@gmail.com")
            assertThat(userEntity.personDao?.firstName).isEqualTo("Tor Egil")
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

        flush { userRepository.save(userToPersist) }

        val userById = userRepository.findByUsername("smuggler")
        val userEntity = userById.orElseThrow { userNotFound() }

        assertAll {
            assertThat(userEntity.personDao).isEqualTo(userToPersist.personDao)
            assertThat(userEntity.username).isEqualTo("smuggler")
            assertThat(userEntity.emailAddress).isEqualTo("smuggle.fast@tantooine.com")
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

        flush { userRepository.save(userToPersist) }

        val lukewarm = "lukewarm"
        userToPersist.username = lukewarm
        userToPersist.emailAddress = "luke@force.com"

        flush { userRepository.save(userToPersist) }

        val userByName = userRepository.findByUsername(lukewarm)
        val userEntity = userByName.orElseThrow { userNotFound() }

        assertAll {
            assertThat(userEntity.username).isEqualTo(lukewarm)
            assertThat(userEntity.emailAddress).isEqualTo("luke@force.com")
        }
    }

    private fun userNotFound(): AssertionError {
        return AssertionError("no user found")
    }

    @Test
    fun `should find active users and admins`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        )

        val spidyPerson = initPerson(address = address, surname = "Parker")
        val superPerson = initPerson(address = address, surname = "Kent")
        val userEntity = initUser(
            persistent = Persistent(),
            person = spidyPerson,
            emailAddress = null,
            username = "spiderman"
        ).toUserDao()

        flush {
            userRepository.save(userEntity)
            userRepository.save(
                initUser(
                    person = superPerson,
                    emailAddress = null,
                    username = "superman",
                    usertype = User.Usertype.INACTIVE
                ).toUserDao()
            )
        }

        val usernames = userRepository.findByUserTypeIn(listOf(UserDao.UserType.ACTIVE, UserDao.UserType.ADMIN))
            .map(UserDao::username)

        assertThat(usernames).containsAtLeast("tip", "spiderman", "jactor")
    }
}