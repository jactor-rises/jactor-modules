package com.github.jactor.persistence

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class BlogRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
    private val blogRepository: BlogRepository = BlogRepositoryObject

    @Test
    fun `should save and then read blog entity`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        )

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE
        )

        val blogToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).toBlogDao()

        blogRepository.save(blogDao = blogToSave)

        val blogs = blogRepository.findBlogs()
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("Blah")
        }
    }

    @Test
    fun `should save then update and read blog entity`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        )

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE,
        )

        val blogToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).toBlogDao()

        blogRepository.save(blogDao = blogToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)

        val blogEntitySaved = blogs.first()
        blogEntitySaved.title = "Duh"

        blogRepository.save(blogEntitySaved)

        val modifiedBlogs = blogRepository.findBlogsByTitle("Duh")
        assertThat(modifiedBlogs).hasSize(1)
        val blogEntity: BlogDao = modifiedBlogs.first()

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("Duh")
        }
    }

    @Test
    fun `should find blog by title`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        )

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE,
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
    fun `should be able to relate a blog entry`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        )

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "black",
            usertype = User.Usertype.ACTIVE,
        )

        val blog = Blog(created = LocalDate.now(), title = "Blah", user = user)
        val blogToSave: BlogDao = blog.toBlogDao()
        val blogEntry = BlogEntry(
            blog = blogToSave.toBlog(),
            creatorName = "arnold",
            entry = "i'll be back"
        )

        val blogEntryToSave: BlogEntryDao = blogEntry.toBlogEntryDao()

        blogToSave.add(blogEntryToSave)
        blogRepository.save(blogDao = blogToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()
        assertThat(blogEntity.entries).hasSize(1)
        val blogEntryEntity = blogEntity.entries.iterator().next()

        assertAll {
            assertThat(blogEntryEntity.entry).isEqualTo("i'll be back")
            assertThat(blogEntryEntity.creatorName).isEqualTo("arnold")
        }
    }
}
