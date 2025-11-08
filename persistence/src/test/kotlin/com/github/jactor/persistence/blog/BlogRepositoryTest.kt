package com.github.jactor.persistence.blog

import java.time.LocalDate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.user.User
import com.github.jactor.persistence.user.UserType
import com.github.jactor.shared.test.isNotOlderThan
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class BlogRepositoryTest @Autowired constructor(
    private val blogRepository: BlogRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save and then read blog dao`() {
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

        blogRepository.save(
            blogDao = BlogDao(created = LocalDate.now(), title = "Blah", userId = user.persistent.id)
        )

        val blogDao = blogRepository.findBlogs().firstOrNull() ?: fail { "Unable to find any blogs" }

        assertAll {
            assertThat(blogDao.created).isEqualTo(LocalDate.now())
            assertThat(blogDao.title).isEqualTo("Blah")
        }
    }

    @Test
    fun `should save then update and read blog dao`() {
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

        val blogToSave = BlogDao(created = LocalDate.now(), title = "Blah", userId = user.persistent.id)
        blogRepository.save(blogDao = blogToSave)

        val blogDaoSaved = blogRepository.findBlogsByTitle(title = blogToSave.title).firstOrNull() ?: fail {
            "Unable to find any blogs by title $${blogToSave.title}"
        }

        blogDaoSaved.title = "Duh"

        blogRepository.save(blogDaoSaved)

        val modifiedBlogs = blogRepository.findBlogsByTitle(title = blogDaoSaved.title)
        assertThat(modifiedBlogs, "saved blog with title ${blogDaoSaved.title}").hasSize(1)
        val blogDao: BlogDao = modifiedBlogs.first()

        assertAll {
            assertThat(blogDao.created).isEqualTo(LocalDate.now())
            assertThat(blogDao.title).isEqualTo("Duh")
        }
    }

    @Test
    fun `should find blog by title`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = User(
                personId = person.id,
                emailAddress = "public@services.com",
                username = "black",
                userType = UserType.ACTIVE,
            )
        )

        val blogToSave = Blog(created = LocalDate.now(), title = "Blah", userId = user.id).toBlogDao()

        blogRepository.save(blogDao = blogToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")

        assertAll {
            assertThat(blogs).hasSize(1)
            assertThat(blogs.firstOrNull()).isNotNull()
            assertThat(blogs.firstOrNull()?.created).isEqualTo(LocalDate.now())
        }
    }

    @Test
    fun `should save then read blog entry`() {
        val address = save(
            address = initAddress(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        )

        val person = save(person = initPerson(address = address, surname = "Adder"))
        val user = save(
            user = initUser(
                person = person,
                emailAddress = "public@services.com",
                username = "white",
            )
        )

        val blog = save(blog = initBlog(created = LocalDate.now(), title = "and then some...", userId = user.id))
        val blogEntryDao = BlogEntryDao(
            blogId = blog.persistent.id ?: error("Blog is not persisted!"),
            creatorName = "smith",
            entry = "once upon a time",
        )

        blogRepository.save(blogEntryDao = blogEntryDao)

        val blogEntries = blogRepository.findBlogEntries()

        assertAll {
            assertThat(blogEntries).hasSize(1)
            val blogEntry = blogEntries.firstOrNull()
            assertThat(blogEntry?.creatorName).isEqualTo("smith")
            assertThat(blogEntry?.timeOfCreation).isNotNull().isNotOlderThan(seconds = 1)
            assertThat(blogEntry?.entry).isEqualTo("once upon a time")
        }
    }
}
