package com.github.jactor.persistence

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class BlogRepositoryTest @Autowired constructor(
    private val blogRepository: BlogRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save and then read blog entity`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        ).withId()

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).withId().toEntity()

        blogRepository.insertOrUpdate(blogEntityToSave)

        val blogs = blogRepository.findAll().toList()
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
        ).withId()

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).withId().toEntity()

        blogRepository.insertOrUpdate(blogEntityToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)

        val blogEntitySaved = blogs.iterator().next()
        blogEntitySaved.title = "Duh"

        blogRepository.insertOrUpdate(blogEntitySaved)

        val modifiedBlogs = blogRepository.findBlogsByTitle("Duh")
        assertThat(modifiedBlogs).hasSize(1)
        val blogEntity: BlogDao = modifiedBlogs.iterator().next()

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
        ).withId()

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = Blog(created = LocalDate.now(), title = "Blah", user = user).withId().toEntity()

        blogRepository.insertOrUpdate(blogEntityToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")

        assertAll {
            assertThat(blogs).hasSize(1)
            assertThat(blogs[0]).isNotNull()
            assertThat(blogs[0].created).isEqualTo(LocalDate.now())

        }
    }

    @Test
    fun `should be able to relate a blog entry`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        ).withId()

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blog = Blog(created = LocalDate.now(), title = "Blah", user = user).withId()
        val blogEntityToSave: BlogDao = blog.toEntity()
        val blogEntry = BlogEntry(
            blog = blogEntityToSave.toBlog(),
            creatorName = "arnold",
            entry = "i'll be back"
        )

        val blogEntryToSave: BlogEntryDao = blogEntry.withId().toBlogEntryDao()

        blogEntityToSave.add(blogEntryToSave)
        blogRepository.insertOrUpdate(blogEntityToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()
        assertThat(blogEntity.getEntries()).hasSize(1)
        val blogEntryEntity = blogEntity.getEntries().iterator().next()

        assertAll {
            assertThat(blogEntryEntity.entry).isEqualTo("i'll be back")
            assertThat(blogEntryEntity.creatorName).isEqualTo("arnold")
        }
    }
}
