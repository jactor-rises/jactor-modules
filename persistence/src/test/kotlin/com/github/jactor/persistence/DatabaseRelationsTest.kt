package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

class DatabaseRelationsTest @Autowired constructor(
    private val addressRepository: AddressRepository,
    private val blogRepository: BlogRepository,
    private val guestBookRepository: GuestBookRepository,
    private val personRepository: PersonRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should be able to relate a blog entry from a user`() {
        val address = save(
            address = initAddress(
                zipCode = "1001",
                addressLine1 = "Test Boulevard 1",
                city = "Testing"
            )
        )

        val person = personRepository.save(
            personDao = initPerson(
                address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
            ).toPersonDao()
        ).toPerson()

        val user = userRepository.save(
            userDao = User(
                person = person,
                emailAddress = "public@services.com",
                username = "black",
                usertype = User.Usertype.ACTIVE,
            ).toUserDao()
        ).toUser()

        val blog = blogRepository.save(
            blogDao = initBlog(
                created = LocalDate.now(), title = "Blah", user = user
            ).toBlogDao()
        ).toBlog()

        blogRepository.save(
            blogEntryDao = initBlogEntry(
                blog = blog,
                creatorName = "arnold",
                entry = "i'll be back"
            ).toBlogEntryDao()
        )

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs, "no of blogs").hasSize(1)

        val blogEntity = blogs.iterator().next()
        assertThat(blogEntity.entries, "no of entries").hasSize(1)

        val blogEntryEntity = blogEntity.entries.iterator().next()

        assertAll {
            assertThat(blogEntryEntity.entry).isEqualTo("i'll be back")
            assertThat(blogEntryEntity.creatorName).isEqualTo("arnold")
        }
    }

    @Test
    fun `should find blog by title related to a user`() {
        val address = save(
            address = initAddress(
                zipCode = "1001",
                addressLine1 = "Test Boulevard 1",
                city = "Testing"
            )
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = User(
                person = person,
                emailAddress = "public@services.com",
                username = "black",
                usertype = User.Usertype.ACTIVE,
            )
        )

        val blogToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).toBlogDao()

        blogRepository.save(blogDao = blogToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")

        assertAll {
            assertThat(blogs).hasSize(1)
            assertThat(blogs.firstOrNull()).isNotNull()
            assertThat(blogs.firstOrNull()?.created).isEqualTo(LocalDate.now())

        }
    }

    @Test
    fun `should be able to relate a user from a person`() {
        val adder = "Adder"
        val alreadyPresentPeople = personRepository.findAll().count()
        val address = save(
            address = initAddress(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
            )
        )

        val person = save(person = initPerson(address = address, surname = adder))
        save(
            user = initUser(
                persistent = Persistent(id = UUID.randomUUID()),
                person = person,
                emailAddress = "public@services.com",
                username = "black",
                usertype = User.Usertype.ACTIVE,
            )
        )

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
