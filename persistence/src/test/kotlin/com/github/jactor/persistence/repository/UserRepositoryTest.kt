package com.github.jactor.persistence.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.dto.UserInternalDto.Usertype
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import com.github.jactor.persistence.entity.UserEntity
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.isEqualTo
import jakarta.persistence.EntityManager

@SpringBootTest
@Transactional
internal class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: EntityManager

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
        val addressInternalDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personInternalDto = PersonBuilder.new(
            personInternalDto = PersonInternalDto(address = addressInternalDto, surname = "Solo")
        ).personInternalDto

        val userToPersist = UserBuilder.new(
            UserInternalDto(
                person = personInternalDto,
                emailAddress = "smuggle.fast@tantooine.com",
                username = "smuggler"
            )
        ).build()

        userRepository.save(userToPersist)
        entityManager.flush()
        entityManager.clear()

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
        val addressInternalDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val personInternalDto = PersonBuilder.new(
            personInternalDto = PersonInternalDto(address = addressInternalDto, surname = "AA")
        ).personInternalDto

        val userToPersist = UserBuilder.new(
            userDto = UserInternalDto(
                persistentDto = PersistentDto(),
                person = personInternalDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).build()

        userRepository.save(userToPersist)
        entityManager.flush()
        entityManager.clear()

        val lukewarm = "lukewarm"
        userToPersist.username = lukewarm
        userToPersist.emailAddress = "luke@force.com"

        userRepository.save(userToPersist)
        entityManager.flush()
        entityManager.clear()

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
        val addressInternalDto = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressInternalDto

        val spidyPersonInternalDto = PersonBuilder.new(
            personInternalDto = PersonInternalDto(address = addressInternalDto, surname = "Parker")
        ).personInternalDto

        val superPersonInternalDto = PersonBuilder.new(
            personInternalDto = PersonInternalDto(address = addressInternalDto, surname = "Kent")
        ).personInternalDto

        val userEntity = UserBuilder.new(
            UserInternalDto(PersistentDto(), spidyPersonInternalDto, null, "spiderman")
        ).build()

        userRepository.save(userEntity)
        userRepository.save(
            UserBuilder.new(
                userDto = UserInternalDto(
                    person = superPersonInternalDto,
                    emailAddress = null,
                    username = "superman",
                    usertype = Usertype.INACTIVE
                )
            ).build()
        )
        entityManager.flush()
        entityManager.clear()

        val usernames = userRepository.findByUserTypeIn(listOf(UserEntity.UserType.ACTIVE, UserEntity.UserType.ADMIN))
            .map(UserEntity::username)

        assertThat(usernames).containsAtLeast("tip", "spiderman", "jactor")
    }
}
