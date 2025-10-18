package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*

class DatabaseRelationsTest @Autowired constructor(
    private val blogRepository: BlogRepository,
    private val personRepository: PersonRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should be able to get a blog entry from the related entries on the blog`() {
        val address = save(
            address = initAddress(
                zipCode = "1001",
                addressLine1 = "Test Boulevard 1",
                city = "Testing"
            )
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "public@services.com",
                username = "black",
            )
        )

        val blog = save(
            blog = initBlog(created = LocalDate.now(), title = "Blah", user = user)
        )

        save(
            blogEntry = initBlogEntry(
                blog = blog,
                creatorName = "arnold",
                entry = "i'll be back"
            )
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

    @Test
    fun `should be able to relate a blog entries from a blog`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "public@services.com",
                username = "black",
            )
        )

        val blog = save(blog = Blog(created = LocalDate.now(), title = "Blah", user = user))
        save(blogEntry = BlogEntry(blog = blog, creatorName = "arnold", entry = "i'll be back"))

        val blogEntity = blogRepository.findBlogsByTitle(title = blog.title).firstOrNull() ?: fail {
            "Unable to find any blogs by title ${blog.title}"
        }

        assertThat(blogEntity.entries).hasSize(1)
        val blogEntryEntity = blogEntity.entries.iterator().next()

        assertAll {
            assertThat(blogEntryEntity.entry).isEqualTo("i'll be back")
            assertThat(blogEntryEntity.creatorName).isEqualTo("arnold")
        }
    }
}
