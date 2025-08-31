package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.withId
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
            assertThat(userEntity.person?.firstName).isEqualTo("Tor Egil")
        }
    }

    @Test
    fun `should write then read a user entity`() {
        val address = Address(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = PersonBuilder.new(
            person = Person(address = address, surname = "Solo")
        ).person

        val userToPersist = UserBuilder.new(
            User(
                person = person,
                emailAddress = "smuggle.fast@tantooine.com",
                username = "smuggler"
            )
        ).build()

        flush { userRepository.save(userToPersist) }

        val userById = userRepository.findByUsername("smuggler")
        val userEntity = userById.orElseThrow { userNotFound() }

        assertAll {
            assertThat(userEntity.person).isEqualTo(userToPersist.person)
            assertThat(userEntity.username).isEqualTo("smuggler")
            assertThat(userEntity.emailAddress).isEqualTo("smuggle.fast@tantooine.com")
        }
    }

    @Test
    fun `should write then update and read a user entity`() {
        val address = Address(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = PersonBuilder.new(
            person = Person(address = address, surname = "AA")
        ).person

        val userToPersist = UserBuilder.new(
            userDto = User(
                persistent = Persistent(),
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).build()

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
        val address = Address(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val spidyPerson = PersonBuilder.new(
            person = Person(address = address, surname = "Parker")
        ).person

        val superPerson = PersonBuilder.new(
            person = Person(address = address, surname = "Kent")
        ).person

        val userEntity = UserBuilder.new(
            User(Persistent(), spidyPerson, null, "spiderman")
        ).build()

        flush {
            userRepository.save(userEntity)
            userRepository.save(
                UserBuilder.new(
                    userDto = User(
                        person = superPerson,
                        emailAddress = null,
                        username = "superman",
                        usertype = User.Usertype.INACTIVE
                    )
                ).build()
            )
        }

        val usernames = userRepository.findByUserTypeIn(listOf(UserEntity.UserType.ACTIVE, UserEntity.UserType.ADMIN))
            .map(UserEntity::username)

        assertThat(usernames).containsAtLeast("tip", "spiderman", "jactor")
    }
}