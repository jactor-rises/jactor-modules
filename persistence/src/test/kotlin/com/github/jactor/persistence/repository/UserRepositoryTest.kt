package com.github.jactor.persistence.repository

import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.address.AddressModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.person.PersonModel
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.user.UserModel.Usertype
import com.github.jactor.persistence.address.AddressBuilder
import com.github.jactor.persistence.person.PersonBuilder
import com.github.jactor.persistence.user.UserBuilder
import com.github.jactor.persistence.user.UserEntity
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.isEqualTo

internal class UserRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
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
        val addressModel = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personModel = PersonBuilder.new(
            personModel = PersonModel(address = addressModel, surname = "Solo")
        ).personModel

        val userToPersist = UserBuilder.new(
            UserModel(
                person = personModel,
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
        val addressModel = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personModel = PersonBuilder.new(
            personModel = PersonModel(address = addressModel, surname = "AA")
        ).personModel

        val userToPersist = UserBuilder.new(
            userDto = UserModel(
                persistentModel = PersistentModel(),
                person = personModel,
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
        val addressModel = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val spidyPersonModel = PersonBuilder.new(
            personModel = PersonModel(address = addressModel, surname = "Parker")
        ).personModel

        val superPersonModel = PersonBuilder.new(
            personModel = PersonModel(address = addressModel, surname = "Kent")
        ).personModel

        val userEntity = UserBuilder.new(
            UserModel(PersistentModel(), spidyPersonModel, null, "spiderman")
        ).build()

        flush {
            userRepository.save(userEntity)
            userRepository.save(
                UserBuilder.new(
                    userDto = UserModel(
                        person = superPersonModel,
                        emailAddress = null,
                        username = "superman",
                        usertype = Usertype.INACTIVE
                    )
                ).build()
            )
        }

        val usernames = userRepository.findByUserTypeIn(listOf(UserEntity.UserType.ACTIVE, UserEntity.UserType.ADMIN))
            .map(UserEntity::username)

        assertThat(usernames).containsAtLeast("tip", "spiderman", "jactor")
    }
}
